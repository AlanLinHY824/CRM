package com.powernode.workbench.controller;

import com.powernode.setting.pojo.TblUser;
import com.powernode.util.PageResult;
import com.powernode.util.Result;
import com.powernode.workbench.pojo.*;
import com.powernode.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @AlanLin 2020/8/29
 */
@RestController
@RequestMapping("tran")
public class TranController {

    @Autowired
    private TranService tranService;

    @Value("${user_session}")
    private String USER_SESSION;

    @RequestMapping("users")
    public Result getUsers(HttpSession session){
        ServletContext servletContext = session.getServletContext();
        List<TblUser> users = tranService.getUsers();
        Map<String,Object> map=new HashMap<>();
        map.put("stage", servletContext.getAttribute("stage"));
        map.put("transactionType", servletContext.getAttribute("transactionType"));
        map.put("source", servletContext.getAttribute("source"));
        map.put("users",users);
        return Result.success(map);
    }

    @RequestMapping("customers")
    public Result getCustomers(@RequestParam(required = false) String customerId,@RequestParam(required = false)  String name){
        List<Map<String, String>> customers = tranService.getCustomers(customerId,name);
        return Result.success(customers);
    }

    @RequestMapping("getposs")
    public Result getposs(HttpSession session){
        ServletContext servletContext = session.getServletContext();
        return Result.success(servletContext.getAttribute("poss"));
    }

    @RequestMapping("getContacts")
    public Result getContacts(String customerName,String search,int pageNo, int pageSize){
        PageResult<TblContacts> pageResult=tranService.getContacts(customerName,search,pageNo,pageSize);
        return Result.success(pageResult);
    }

    @RequestMapping("getActivities")
    public Result getActivities(@RequestParam(value = "search",required = false) String search,Integer pageNo,Integer pageSize){
        PageResult<TblActivity> pageResult=tranService.getActivities(search,pageNo,pageSize);
        return Result.success(pageResult);
    }

    @RequestMapping("createTran")
    public Result createTran(TblTran tblTran,HttpSession session){
        TblUser tblUser = (TblUser) session.getAttribute(USER_SESSION);
        tblTran.setCreateby(tblUser.getName());
        tranService.createTran(tblTran);
        return Result.success();
    }
    @RequestMapping("editTran")
    public Result editTran(TblTran tblTran,HttpSession session){
        TblUser tblUser = (TblUser) session.getAttribute(USER_SESSION);
        tblTran.setEditby(tblUser.getName());
        tranService.editTran(tblTran);
        return Result.success();
    }

    @RequestMapping("pageList")
    public Result pageList(@RequestParam(required = false) String fullname,
                           @RequestParam(required = false) String owner,
                           @RequestParam(required = false) String name,
                           @RequestParam(required = false) String customerName,
                           @RequestParam(required = false) String source,
                           @RequestParam(required = false) String stage,
                           @RequestParam(required = false) String type,
                           int pageNo, int pageSize){
        PageResult<TblTran> pageResult = tranService.pageList(fullname, owner, name,customerName, source,stage,type, pageNo, pageSize);
        return Result.success(pageResult);
    }

    @RequestMapping("getById")
    public Result getById(String id,HttpSession session){
        ServletContext servletContext = session.getServletContext();
        Map map = tranService.getItem(id);
        map.put("stage", servletContext.getAttribute("stage"));
        map.put("trantype", servletContext.getAttribute("transactionType"));
        map.put("source", servletContext.getAttribute("source"));
        return Result.success(map);
    }

    @RequestMapping("getDetail")
    public Result getDetail(String tranId,HttpSession session){
        Map<String, String> possMap = (Map<String, String>)session.getServletContext().getAttribute("poss");
        TblTran tblTran=tranService.getDetail(tranId);
        String poss = possMap.get(tblTran.getStage());
        tblTran.setPoss(poss);
        return Result.success(tblTran);
    }

    @RequestMapping("stageIcon")
    public Result stageShow(String tranId,HttpSession session){
        ServletContext servletContext = session.getServletContext();
        Set<TblDicValue> stageSet = (Set<TblDicValue>)servletContext.getAttribute("stage");
        Map<String, String> possMap = (Map<String, String>)servletContext.getAttribute("poss");
        return Result.success(tranService.stageIcon(tranId,stageSet,possMap));
    }

    @RequestMapping("editStage")
    public Result stageShow(String tranId,String stage,HttpSession session){
        TblUser tblUser = (TblUser) session.getAttribute(USER_SESSION);
        String editby = tblUser.getName();
        ServletContext servletContext = session.getServletContext();
        Map<String, String> possMap = (Map<String, String>)servletContext.getAttribute("poss");
        TblTran tblTran = tranService.editStage(tranId, stage, editby, possMap);
        return Result.success(tblTran);
    }
    @RequestMapping("getHistory")
    public Result getHistory(String tranId,HttpSession session){
        Map<String, String> possMap = (Map<String, String>)session.getServletContext().getAttribute("poss");
        List<TblTranHistory> tblTranHistories=tranService.getHistory(tranId,possMap);
        return Result.success(tblTranHistories);
    }


}
