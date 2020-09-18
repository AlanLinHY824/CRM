package com.powernode.setting.controller;

import com.powernode.myenum.ResultEnum;
import com.powernode.setting.pojo.TblUser;
import com.powernode.setting.service.UserService;
import com.powernode.util.Result;
import com.powernode.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Alan Lin
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${user_session}")
    private String USER_SESSION;

    @RequestMapping("login")
    public Result login(TblUser tblUser,String verifycode,HttpServletRequest request, HttpSession session){
//        if (session.getAttribute("VerifyCode")==null){
//            throw new ResultException(ResultEnum.NONE_VERIFYCODE);
//        }
//        if (session.getAttribute("VerifyCode").equals(verifycode)){
//            throw new ResultException(ResultEnum.VERIFYCODE_WRONG);
//        }
        String ip = request.getRemoteAddr();
        tblUser.setAllowips(ip);
        TblUser resTblUser = userService.login(tblUser);
        session.setAttribute(USER_SESSION, resTblUser);
        return Result.success(ResultEnum.SUCCESS);
    }

    @RequestMapping("getLogInfo")
    public Result login(HttpSession session){
        TblUser tblUser = (TblUser) session.getAttribute(USER_SESSION);
        return Result.success(tblUser.getName());
    }

    @RequestMapping("getcode")
    public Result verifyCode(String phone, HttpSession session){
        String scode = UserUtils.verifyCode(phone);
        session.setAttribute("VerifyCode",scode);
        return Result.success();
    }

    @RequestMapping("idVerify")
    public Result idVerify(String id,String name){
        String result = UserUtils.idVerify(id, name);
        return Result.success(result);
    }
}
