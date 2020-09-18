package com.powernode.exception;

import com.powernode.myenum.ResultEnum;

/**
 * @author Alan Lin
 */
public class ResultException extends RuntimeException {
    private ResultEnum resultEnum;
    private String message;
    private Integer code;

    public ResultException(ResultEnum resultEnum) {
        this.resultEnum = resultEnum;
    }

    public ResultException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        if (resultEnum!=null){
            return resultEnum.getMessage();
        }
        return message;
    }

    public Integer getCode() {
        if (resultEnum!=null){
            return resultEnum.getCode();
        }
        return code;
    }
}
