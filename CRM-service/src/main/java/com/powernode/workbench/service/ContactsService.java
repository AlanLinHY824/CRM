package com.powernode.workbench.service;

import com.powernode.setting.pojo.TblUser;
import com.powernode.util.PageResult;
import com.powernode.workbench.pojo.TblContacts;
import com.powernode.workbench.pojo.TblTran;

import java.util.List;
import java.util.Map;

/**
 * @Author AlanLin
 * @Description
 * @Date 2020/9/7
 */
public interface ContactsService {
    PageResult<TblContacts> pageList(String fullname, String owner, String customerName, String source,String birth, int pageNo, int pageSize);

    List<TblUser> getAllUsers();


    Map getbyId(String id);

    void editbyId(TblContacts tblContacts);

    void deleteById(List<String> ids);

    PageResult<TblTran> getTran(String customerId, int pageNo, int pageSize);

    PageResult<TblContacts> getContact(String customerId, int pageNo, int pageSize);

    List<TblUser> getUsers();

    void addContact(TblContacts tblContacts);

    void deleteContact(String contactId);

    List<Map<String, String>> getCustomers(String name);
}
