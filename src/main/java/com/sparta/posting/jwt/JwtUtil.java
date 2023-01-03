package com.sparta.posting.jwt;


import com.sparta.posting.entity.UserRoleEnum;
import com.sparta.posting.security.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j            //로깅에대한 추상 레이어를 제공하는 인터페이스의 모음
@Component       //Bean에 등록하기 위해서
@RequiredArgsConstructor  //선언만으로 객체 생성가능
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";       //토큰시작부분
    public static final long TOKEN_TIME = 60 * 60 * 1000L;

    @Value("${jwt.secret.key}")       //properties의 값을 읽어온다.
    private String secretKey;         //비밀키 jwt암호 해독의 필수키
    private Key key;                   //secretKey를 사용할수있게 변화시킨 key
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final UserDetailsServiceImpl userDetailsService;

    @PostConstruct       //의존성주입이 이루어진후 초기화 하는 매서드 로직이 실행되기전에 수행된다.
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);  //secretKey는 64진법으로 이루어져서 이것을 해독해서 byte코드로 만들고
        key = Keys.hmacShaKeyFor(bytes);                       //Key로 변환시켜 key로 넣어준다
    }

    //클라이언트 요청에서 토큰을 찾아내고 BEARER_PREFIX를 제거하여 key 해독가능한 상태로 만들어준다.
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);  //HttpServletRequest매서드 .getHeader로 request에서 토큰을 찾는다.
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {  //찾은 토큰이 text를 가지고있고 시작부분이 위에 정한 BEARER_PREFIX 와 같다면
            return bearerToken.substring(7); //"Bearer " 를 지워주기위해 substring을 사용한다.
        }
        return null;
    }

    public String createToken(String username, UserRoleEnum role) {              //토큰을 만드는 매서드
        Date date = new Date();

        return  BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime()+ TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key,signatureAlgorithm)
                        .compact();
    }

    //token을 받아와서 key를 이용해 해독하고 해독한 내용에서 오류가 있다면 false를 반환하고 정당한 토큰이면 true를 반환하는 검증 매서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        }  catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    //token을 받아와서 key를 이용해 해독하고 해독한 부분에서 유저 정보를 가져온다. 위에 validateToken 같이 쓰는게 좋다.
    public Claims getUserInformToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }
}
