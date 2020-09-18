package com.powernode.workbench.service;

import com.powernode.setting.pojo.TblUser;
import com.powernode.util.PageResult;
import com.powernode.workbench.pojo.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author AlanLin
 * @Description
 * @Date 2020/9/7
 */
public interface TranService {
    List<TblUser> getUsers();

    List<Map<String,String>> getCustomers(String customerId, String name);

    PageResult<TblContacts> getContacts(String customerName, String search, int pageNo, int pageSize);

    PageResult<TblActivity> getActivities(String search, Integer pageNo, Integer pageSize);

    void createTran(TblTran tblTran);

    PageResult<TblTran> pageList(String fullname, String owner, String name, String customerName, String source, String stage, String type, int pageNo, int pageSize);

    Map getItem(String id);

    void editTran(TblTran tblTran);

    Map<String, Object> stageIcon(String tranId, Set<TblDicValue> stageSet, Map<String, String> possMap);

    TblTran editStage(String tranId, String stage, String editby, Map<String, String> possMap);

    TblTran getDetail(String id);

    List<TblTranHistory> getHistory(String tranId, Map<String, String> possMap);
}
