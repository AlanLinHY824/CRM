package com.powernode.workbench.controller;

import com.powernode.setting.pojo.TblUser;
import com.powernode.util.PageResult;
import com.powernode.util.Result;
import com.powernode.workbench.pojo.TblContacts;
import com.powernode.workbench.service.ContactsService;
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
@RequestMapping("contacts")
public class ContactsController {

    @Autowired
    private ContactsService contactsService;

    @Value("${user_session}")
    private String USER_SESSION;

    @RequestMapping("pageList")
    public Result pageList(@RequestParam(required = false) String fullname,
                           @RequestParam(required = false) String owner,
                           @RequestParam(required = false) String birth,
                           @RequestParam(required = false) String customerName,
                           @RequestParam(required = false) String source,
                           int pageNo, int pageSize){
        PageResult<TblContacts> pageResult = contactsService.pageList(fullname, owner, customerName, source,birth, pageNo, pageSize);
        return Result.success(pageResult);
    }

    @RequestMapping("allUsers")
    public Result getUser(HttpSession session){
        ServletContext servletContext = session.getServletContext();
        List<TblUser> allUsers = contactsService.getAllUsers();
        Map<String,Object> map=new HashMap<>();
        map.put("appellation", servletContext.getAttribute("appellation"));
        map.put("source", servletContext.getAttribute("source"));
        map.put("users",allUsers);
        return Result.success(map);
    }

    @RequestMapping("source")
    public Result getSource(HttpSession session){
        ServletContext servletContext = session.getServletContext();
        return Result.success(servletContext.getAttribute("source"));
    }

    @RequestMapping("customers")
    public Result getCustomers(String name){
        List<Map<String, String>> customers = contactsService.getCustomers(name);
        return Result.success(customers);
    }

    @RequestMapping("add")
    public Result addContact(TblContacts tblContacts, HttpSession session){
        TblUser tblUser = (TblUser) session.getAttribute(USER_SESSION);
        tblContacts.setCreateby(tblUser.getName());
        contactsService.addContact(tblContacts);
        return Result.success();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Result getItem(@PathVariable("id") String id,HttpSession session){
        Map contactsMap=contactsService.getbyId(id);
        ServletContext servletContext = session.getServletContext();
        contactsMap.put("appellation", servletContext.getAttribute("appellation"));
        contactsMap.put("source", servletContext.getAttribute("source"));
        return Result.success(contactsMap);
    }

    @RequestMapping("edit")
    public Result pageList(TblContacts tblContacts, HttpSession session){
        TblUser tblUser = (TblUser) session.getAttribute(USER_SESSION);
        tblContacts.setEditby(tblUser.getName());
        contactsService.editbyId(tblContacts);
        return Result.success();
    }

    @RequestMapping("delete")
    public Result deleteById(@RequestParam("ids[]") List<String> ids){
        contactsService.deleteById(ids);
        return Result.success();
    }

//    @RequestMapping("getTran")
//    public Result getTran(String customerId,int pageNo, int pageSize, HttpSession session){
//        PageResult<TblTran> tblTranList=customerService.getTran(customerId,pageNo, pageSize);
//        ServletContext servletContext = session.getServletContext();
//        Map<String,Object> map=new HashMap<>();
//        map.put("pageResult",tblTranList);
//        map.put("poss",servletContext.getAttribute("poss"));
//        return Result.success(map);
//    }
//
//    @RequestMapping("getContact")
//    public Result getContact(String customerId,int pageNo, int pageSize){
//        PageResult<TblContacts> contacts=customerService.getContact(customerId,pageNo, pageSize);
//        return Result.success(contacts);
//    }
//
//    @RequestMapping("users")
//    public Result getUsers(HttpSession session){
//        ServletContext servletContext = session.getServletContext();
//        List<TblUser> users = customerService.getUsers();
//        Map<String,Object> map=new HashMap<>();
//        map.put("appellation", servletContext.getAttribute("appellation"));
//        map.put("source", servletContext.getAttribute("source"));
//        map.put("users",users);
//        return Result.success(map);
//    }
//
//
//    @RequestMapping("deleteContact")
//    public Result deleteContact(String contactId){
//        customerService.deleteContact(contactId);
//        return Result.success();
//    }

}
