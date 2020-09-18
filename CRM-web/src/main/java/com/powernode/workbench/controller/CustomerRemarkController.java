package com.powernode.workbench.controller;

import com.powernode.setting.pojo.TblUser;
import com.powernode.util.Result;
import com.powernode.workbench.pojo.TblCustomerRemark;
import com.powernode.workbench.service.CustomerRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * @author Alan Lin
 */
@RestController
@RequestMapping("customerRmk")
public class CustomerRemarkController {
    @Autowired
    CustomerRemarkService customerRemarkService;

    @Value("${user_session}")
    private String USER_SESSION;

    @RequestMapping("getRemarks")
    public Result getRemarks(String customerId){
        List<TblCustomerRemark> remarkList=customerRemarkService.getItems(customerId);
        return Result.success(remarkList);
    }

    @RequestMapping("addRemark")
    public Result addRemark(TblCustomerRemark tblCustomerRemark, HttpSession session){
        TblUser tblUser = (TblUser) session.getAttribute(USER_SESSION);
        tblCustomerRemark.setCreateby(tblUser.getName());
        customerRemarkService.addItem(tblCustomerRemark);
        return Result.success();
    }

    @RequestMapping("editRemark")
    public Result editRemark(TblCustomerRemark tblCustomerRemark, HttpSession session){
        TblUser tblUser = (TblUser) session.getAttribute(USER_SESSION);
        tblCustomerRemark.setEditby(tblUser.getName());
        customerRemarkService.editItem(tblCustomerRemark);
        return Result.success();
    }

    @RequestMapping("deleteRemark")
    public Result deleteRemark(String remarkId){
        customerRemarkService.deleteItem(remarkId);
        return Result.success();
    }

}
