package com.powernode.workbench.service;

import com.powernode.workbench.pojo.TblCustomerRemark;

import java.util.List;

/**
 * @Author AlanLin
 * @Description
 * @Date 2020/9/3
 */
public interface CustomerRemarkService {

    List<TblCustomerRemark> getItems(String customerId);

    void addItem(TblCustomerRemark tblCustomerRemark);

    void editItem(TblCustomerRemark tblCustomerRemark);

    void deleteItem(String remarkId);

}
