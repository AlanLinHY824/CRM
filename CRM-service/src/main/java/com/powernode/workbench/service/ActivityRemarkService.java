package com.powernode.workbench.service;

import com.powernode.workbench.pojo.TblActivityRemark;

import java.util.List;

/**
 * @Author AlanLin
 * @Description
 * @Date 2020/9/3
 */
public interface ActivityRemarkService {
    /**
     * 查询市场活动备注
     * @param activityId
     * @return
     */
    List<TblActivityRemark> getRemarksById(String activityId);

    /**
     * 添加市场活动备注
     * @param tblActivityRemark
     */
    void addItem(TblActivityRemark tblActivityRemark);

    void editItem(TblActivityRemark tblActivityRemark);

    void delItem(String id);
}
