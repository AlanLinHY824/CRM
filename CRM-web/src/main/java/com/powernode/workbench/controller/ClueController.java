package com.powernode.workbench.controller;

import com.powernode.setting.pojo.TblUser;
import com.powernode.util.PageResult;
import com.powernode.util.Result;
import com.powernode.workbench.pojo.TblActivity;
import com.powernode.workbench.pojo.TblClue;
import com.powernode.workbench.pojo.TblTran;
import com.powernode.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author AlanLin
 * @Description
 * @Date 2020/9/3
 */
@RestController
@RequestMapping("clue")
public class ClueController {

    @Autowired
    private ClueService clueService;

    @Value("${user_session}")
    private String USER_SESSION;

    @RequestMapping("pageList")
    public Result pageList(@RequestParam(required = false) String fullname,
                           @RequestParam(required = false) String company,
                           @RequestParam(required = false) String phone,
                           @RequestParam(required = false) String source,
                           @RequestParam(required = false) String owner,
                           @RequestParam(required = false) String mphone,
                           @RequestParam(required = false) String state,
                           int pageNo,int pageSize){
        PageResult<TblClue> pageResult = clueService.pageList(fullname, company, phone, source,owner,mphone,state, pageNo, pageSize);
        return Result.success(pageResult);
    }

    @RequestMapping("users")
    public Result getUsers(HttpSession session){
        ServletContext servletContext = session.getServletContext();
        List<TblUser> users = clueService.getUsers();
        Map<String,Object> map=new HashMap<>();
        map.put("appellation", servletContext.getAttribute("appellation"));
        map.put("clueState", servletContext.getAttribute("clueState"));
        map.put("source", servletContext.getAttribute("source"));
        map.put("users",users);
        return Result.success(map);
    }

    @RequestMapping("add")
    public Result add(TblClue tblClue,HttpSession session){
        TblUser tblUser = (TblUser) session.getAttribute(USER_SESSION);
        tblClue.setCreateby(tblUser.getName());
        clueService.addItem(tblClue);
        return Result.success();
    }

    @RequestMapping("{id}")
    public Result getItem(@PathVariable("id") String id,HttpSession session){
        Map<String, Object> clue = clueService.getItem(id);
        ServletContext servletContext = session.getServletContext();
        clue.put("appellation", servletContext.getAttribute("appellation"));
        clue.put("clueState", servletContext.getAttribute("clueState"));
        clue.put("source", servletContext.getAttribute("source"));
        return Result.success(clue);
    }

    @RequestMapping("edit")
    public Result edit(TblClue tblClue,HttpSession session){
        TblUser tblUser = (TblUser) session.getAttribute(USER_SESSION);
        tblClue.setEditby(tblUser.getName());
        clueService.editItem(tblClue);
        return Result.success();
    }

    @RequestMapping("delete")
    public Result delete(@RequestParam("ids[]") List<String> ids){
        clueService.delItem(ids);
        return Result.success();
    }

    @RequestMapping("detail")
    public Result getDetail(String clueId){
        TblClue tblClue=clueService.getDetail(clueId);
        return Result.success(tblClue);
    }

    @RequestMapping("getRelation")
    public Result getRelation(@RequestParam(value = "search",required = false) String search,String clueId,Integer pageNo,Integer pageSize){
        PageResult<TblActivity> pageResult=clueService.getRelation(search,clueId,pageNo,pageSize);
        return Result.success(pageResult);
    }

    @RequestMapping("getActivities")
    public Result getActivities(@RequestParam(value = "search",required = false) String search,Integer pageNo,Integer pageSize){
        PageResult<TblActivity> pageResult=clueService.getActivities(search,pageNo,pageSize);
        return Result.success(pageResult);
    }

    @RequestMapping("addRelation")
    public Result addRelation(@RequestParam(value = "actIds[]") List<String> actIds,String clueId){
        clueService.addRelation(actIds,clueId);
        return Result.success();
    }

    @RequestMapping("delRelation")
    public Result delRelation(String actId,String clueId){
        clueService.delRelation(actId,clueId);
        return Result.success();
    }

    @RequestMapping("stage")
    public Result getStage(HttpSession session){
        ServletContext servletContext = session.getServletContext();
        return Result.success(servletContext.getAttribute("stage"));
    }

    @RequestMapping("transferClue")
    public Result transferClue(TblTran tblTran,String clueId,HttpSession session,@RequestParam(required = false,defaultValue = "0") String flag){
        TblUser tblUser = (TblUser) session.getAttribute(USER_SESSION);
        clueService.transferClue(tblTran,tblUser.getName(),clueId,flag);
        return Result.success();
    }

}
