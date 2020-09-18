package com.powernode.util;

import com.powernode.exception.ResultException;

/**
 * @AlanLin 2020/8/29
 */
public class Result {
    private String message;
    private Integer code;
    private Object data;
    public Result(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    private Result() {
        this.message = "SUCCESS";
        this.code = 1001;
    }

    private Result(Object data) {
        this.message = "SUCCESS";
        this.code = 1001;
        this.data=data;
    }

    public static Result success(Object data){
        return new Result(data);
    }

    public static Result success(){
        return new Result();
    }

    public static Result fail(ResultException e){
        return new Result(e.getMessage(),e.getCode());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
