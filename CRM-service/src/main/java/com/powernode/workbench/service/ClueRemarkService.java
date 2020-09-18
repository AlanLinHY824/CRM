package com.powernode.workbench.service;

import com.powernode.workbench.pojo.TblClueRemark;

import java.util.List;

/**
 * @Author AlanLin
 * @Description
 * @Date 2020/9/3
 */
public interface ClueRemarkService {

    void addItem(TblClueRemark tblClueRemark);

    List<TblClueRemark> getItems(String clueId);

    void editItem(TblClueRemark tblClueRemark);

    void deleteItem(String remarkId);
}
