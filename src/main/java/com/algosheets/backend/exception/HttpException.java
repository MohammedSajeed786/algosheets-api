package com.algosheets.backend.exception;


import lombok.Data;

@Data
public class HttpException extends RuntimeException{

    private int statusCode;
   public HttpException(String message,int statusCode){
       super(message);
       this.statusCode=statusCode;
   }
}
