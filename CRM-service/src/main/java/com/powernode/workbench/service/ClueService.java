package com.powernode.workbench.service;

import com.powernode.setting.pojo.TblUser;
import com.powernode.util.PageResult;
import com.powernode.workbench.pojo.TblActivity;
import com.powernode.workbench.pojo.TblClue;
import com.powernode.workbench.pojo.TblTran;

import java.util.List;
import java.util.Map;

/**
 * @Author AlanLin
 * @Description
 * @Date 2020/9/3
 */
public interface ClueService {

    PageResult<TblClue> pageList(String fullname, String company, String phone, String source, String owner, String mphone, String state, int pageNo, int pageSize);

    List<TblUser> getUsers();

    void addItem(TblClue tblClue);

    Map<String,Object> getItem(String id);

    void editItem(TblClue tblClue);

    void delItem(List<String> id);

    TblClue getDetail(String clueId);

    PageResult<TblActivity> getRelation(String search, String clueId, Integer pageNo, Integer pageSize);

    PageResult<TblActivity> getActivities(String search, Integer pageNo, Integer pageSize);

    void addRelation(List<String> actIds, String clueId);

    void delRelation(String actId, String clueId);

    void transferClue(TblTran tblTran, String createby, String clueId, String flag);
}
