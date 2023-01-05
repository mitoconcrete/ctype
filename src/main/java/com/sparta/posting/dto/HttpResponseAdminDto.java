package com.sparta.posting.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HttpResponseAdminDto {
    private String msg;

    private int statusCode;

    public HttpResponseAdminDto(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
