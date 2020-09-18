package com.powernode.workbench.service;

import com.powernode.setting.pojo.TblUser;
import com.powernode.util.PageResult;
import com.powernode.workbench.pojo.TblContacts;
import com.powernode.workbench.pojo.TblCustomer;
import com.powernode.workbench.pojo.TblTran;

import java.util.List;
import java.util.Map;

/**
 * @Author AlanLin
 * @Description
 * @Date 2020/9/7
 */
public interface CustomerService {
    PageResult<TblCustomer> pageList(String name, String owner, String phone, String website, int pageNo, int pageSize);

    List<TblUser> getAllUsers();

    void addCustomer(TblCustomer tblCustomer);

    Map getbyId(String id);

    void editbyId(TblCustomer tblCustomer);

    void deleteById(List<String> ids);

    PageResult<TblTran> getTran(String customerId, int pageNo, int pageSize);

    PageResult<TblContacts> getContact(String customerId, int pageNo, int pageSize);

    List<TblUser> getUsers();

    void addContact(TblContacts tblContacts);

    void deleteContact(String contactId);
}
