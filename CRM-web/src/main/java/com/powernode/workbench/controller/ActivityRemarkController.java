package com.powernode.workbench.controller;

import com.powernode.setting.pojo.TblUser;
import com.powernode.util.Result;
import com.powernode.workbench.pojo.TblActivityRemark;
import com.powernode.workbench.service.ActivityRemarkService;
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
@RequestMapping("actRemark")
public class ActivityRemarkController {

    @Autowired
    private ActivityRemarkService remarkService;

    @Value("${user_session}")
    private String USER_SESSION;

    @RequestMapping("remarksByAct")
    public Result getRemarksById(String activityId){
        List<TblActivityRemark> remarkList = remarkService.getRemarksById(activityId);
        return Result.success(remarkList);
    }

    @RequestMapping("addItem")
    public Result addItem(TblActivityRemark tblActivityRemark, HttpSession session){
        TblUser tblUser = (TblUser) session.getAttribute(USER_SESSION);
        tblActivityRemark.setCreateby(tblUser.getName());
        remarkService.addItem(tblActivityRemark);
        return Result.success();
    }

    @RequestMapping("editItem")
    public Result editItem(TblActivityRemark tblActivityRemark, HttpSession session){
        TblUser tblUser = (TblUser) session.getAttribute(USER_SESSION);
        tblActivityRemark.setEditby(tblUser.getName());
        remarkService.editItem(tblActivityRemark);
        return Result.success();
    }

    @RequestMapping("delItem")
    public Result delItem(String id){
        remarkService.delItem(id);
        return Result.success();
    }
}
