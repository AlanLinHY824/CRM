package com.powernode.workbench.controller;

import com.powernode.setting.pojo.TblUser;
import com.powernode.util.PageResult;
import com.powernode.util.Result;
import com.powernode.workbench.pojo.TblActivity;
import com.powernode.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @AlanLin 2020/8/29
 */
@RestController()
@RequestMapping("activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Value("${user_session}")
    private String USER_SESSION;

    @RequestMapping("allUsers")
    public Result getUser(){
        List<TblUser> allUsers = activityService.getAllUsers();
        return Result.success(allUsers);
    }

    @RequestMapping("add")
    public Result addActivity(@RequestBody TblActivity tblActivity, HttpSession session){
        TblUser tblUser = (TblUser) session.getAttribute(USER_SESSION);
        tblActivity.setCreateby(tblUser.getName());
        activityService.addActivity(tblActivity);
        return Result.success();
    }

    @RequestMapping("pageList")
    public Result pageList(@RequestParam(required = false) String name,
                           @RequestParam(required = false) String owner,
                           @RequestParam(required = false) String startdate,
                           @RequestParam(required = false) String enddate,
                           int pageNo,int pageSize){
        PageResult<TblActivity> pageResult = activityService.pageList(name, owner, startdate, enddate, pageNo, pageSize);
        return Result.success(pageResult);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Result getItem(@PathVariable("id") String id){
        Map activityMap=activityService.getbyId(id);
        return Result.success(activityMap);
    }

    @RequestMapping("edit")
    public Result pageList(TblActivity tblActivity,HttpSession session){
        TblUser tblUser = (TblUser) session.getAttribute(USER_SESSION);
        tblActivity.setEditby(tblUser.getName());
        activityService.editbyId(tblActivity);
        return Result.success();
    }

    @RequestMapping("delete")
    public Result deleteById(@RequestParam("ids[]") List<String> ids){
        activityService.deleteById(ids);
        return Result.success();
    }


}
