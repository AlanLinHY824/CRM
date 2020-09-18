package com.powernode.workbench.controller;

import com.powernode.setting.pojo.TblUser;
import com.powernode.util.PageResult;
import com.powernode.util.Result;
import com.powernode.workbench.pojo.TblContacts;
import com.powernode.workbench.pojo.TblCustomer;
import com.powernode.workbench.pojo.TblTran;
import com.powernode.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @AlanLin 2020/8/29
 */
@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Value("${user_session}")
    private String USER_SESSION;

    @RequestMapping("pageList")
    public Result pageList(@RequestParam(required = false) String name,
                           @RequestParam(required = false) String owner,
                           @RequestParam(required = false) String phone,
                           @RequestParam(required = false) String website,
                           int pageNo, int pageSize){
        PageResult<TblCustomer> pageResult = customerService.pageList(name, owner, phone, website, pageNo, pageSize);
        return Result.success(pageResult);
    }

    @RequestMapping("allUsers")
    public Result getUser(){
        List<TblUser> allUsers = customerService.getAllUsers();
        return Result.success(allUsers);
    }

    @RequestMapping("add")
    public Result addCustomer(@RequestBody TblCustomer tblCustomer, HttpSession session){
        TblUser tblUser = (TblUser) session.getAttribute(USER_SESSION);
        tblCustomer.setCreateby(tblUser.getName());
        customerService.addCustomer(tblCustomer);
        return Result.success();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Result getItem(@PathVariable("id") String id){
        Map customerMap=customerService.getbyId(id);
        return Result.success(customerMap);
    }

    @RequestMapping("edit")
    public Result pageList(TblCustomer tblCustomer, HttpSession session){
        TblUser tblUser = (TblUser) session.getAttribute(USER_SESSION);
        tblCustomer.setEditby(tblUser.getName());
        customerService.editbyId(tblCustomer);
        return Result.success();
    }

    @RequestMapping("delete")
    public Result deleteById(@RequestParam("ids[]") List<String> ids){
        customerService.deleteById(ids);
        return Result.success();
    }

    @RequestMapping("getTran")
    public Result getTran(String customerId,int pageNo, int pageSize, HttpSession session){
        PageResult<TblTran> tblTranList=customerService.getTran(customerId,pageNo, pageSize);
        ServletContext servletContext = session.getServletContext();
        Map<String,Object> map=new HashMap<>();
        map.put("pageResult",tblTranList);
        map.put("poss",servletContext.getAttribute("poss"));
        return Result.success(map);
    }

    @RequestMapping("getContact")
    public Result getContact(String customerId,int pageNo, int pageSize){
        PageResult<TblContacts> contacts=customerService.getContact(customerId,pageNo, pageSize);
        return Result.success(contacts);
    }

    @RequestMapping("users")
    public Result getUsers(HttpSession session){
        ServletContext servletContext = session.getServletContext();
        List<TblUser> users = customerService.getUsers();
        Map<String,Object> map=new HashMap<>();
        map.put("appellation", servletContext.getAttribute("appellation"));
        map.put("source", servletContext.getAttribute("source"));
        map.put("users",users);
        return Result.success(map);
    }

    @RequestMapping("addContact")
    public Result addContact(TblContacts tblContacts,HttpSession session){
        TblUser tblUser = (TblUser) session.getAttribute(USER_SESSION);
        tblContacts.setCreateby(tblUser.getName());
        customerService.addContact(tblContacts);
        return Result.success();
    }

    @RequestMapping("deleteContact")
    public Result deleteContact(String contactId){
        customerService.deleteContact(contactId);
        return Result.success();
    }

}
