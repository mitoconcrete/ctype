package com.sparta.posting.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class ResponseDto extends Throwable {
    private String msg;

    private int statusCode;

    public ResponseDto(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
