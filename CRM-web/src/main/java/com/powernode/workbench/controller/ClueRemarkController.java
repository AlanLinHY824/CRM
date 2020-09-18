package com.powernode.workbench.controller;

import com.powernode.setting.pojo.TblUser;
import com.powernode.util.Result;
import com.powernode.workbench.pojo.TblClueRemark;
import com.powernode.workbench.service.ClueRemarkService;
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
@RequestMapping("clueRemark")
public class ClueRemarkController {
    @Autowired
    ClueRemarkService clueRemarkService;

    @Value("${user_session}")
    private String USER_SESSION;

    @RequestMapping("getRemarks")
    public Result getRemarks(String clueId){
        List<TblClueRemark> remarkList=clueRemarkService.getItems(clueId);
        return Result.success(remarkList);
    }

    @RequestMapping("addRemark")
    public Result addRemark(TblClueRemark tblClueRemark, HttpSession session){
        TblUser tblUser = (TblUser) session.getAttribute(USER_SESSION);
        tblClueRemark.setCreateby(tblUser.getName());
        clueRemarkService.addItem(tblClueRemark);
        return Result.success();
    }

    @RequestMapping("editRemark")
    public Result editRemark(TblClueRemark tblClueRemark, HttpSession session){
        TblUser tblUser = (TblUser) session.getAttribute(USER_SESSION);
        tblClueRemark.setEditby(tblUser.getName());
        clueRemarkService.editItem(tblClueRemark);
        return Result.success();
    }

    @RequestMapping("deleteRemark")
    public Result deleteRemark(String remarkId){
        clueRemarkService.deleteItem(remarkId);
        return Result.success();
    }
}
