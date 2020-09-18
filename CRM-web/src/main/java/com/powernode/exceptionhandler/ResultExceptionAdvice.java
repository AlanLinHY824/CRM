package com.powernode.exceptionhandler;

import com.powernode.exception.ResultException;
import com.powernode.myenum.ResultEnum;
import com.powernode.util.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Alan Lin
 */
@ControllerAdvice
public class ResultExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handlerExceptionAdvice(Exception e){
        if (e instanceof ResultException){
            return Result.fail((ResultException) e);
        }else {
            e.printStackTrace();
            return Result.fail(new ResultException(ResultEnum.UNKNOWN_ERROR));
        }
    }
}
