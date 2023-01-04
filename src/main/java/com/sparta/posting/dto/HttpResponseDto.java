package com.sparta.posting.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HttpResponseDto {
    private String msg;

    private int statusCode;

    public HttpResponseDto(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
