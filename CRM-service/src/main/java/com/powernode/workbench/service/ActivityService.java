package com.powernode.workbench.service;

import com.powernode.setting.pojo.TblUser;
import com.powernode.util.PageResult;
import com.powernode.workbench.pojo.TblActivity;

import java.util.List;
import java.util.Map;

/**
 * @AlanLin 2020/8/28
 */
public interface ActivityService {

    /**
     * 获取用户信息
     * @return 返回所有用户信息
     */
    List<TblUser> getAllUsers();

    /**
     * 添加市场活动
     * @param tblActivity 市场活动对象
     */
    void addActivity(TblActivity tblActivity);

    /**
     * 分页显示市场活动
     * @param name 模糊查询条件：市场活动名称
     * @param owner 模糊查询条件：市场活动拥有者
     * @param startdate 模糊查询条件：市场活动开始时间
     * @param enddate 模糊查询条件：市场活动结束时间
     * @param pageNo 当前页码
     * @param pageSize 每页记录数
     * @return 返回查询到的当页数据、总记录数以及总页数
     */
    PageResult<TblActivity> pageList(String name, String owner, String startdate, String enddate, int pageNo, int pageSize);

    /**
     *
     * @param id activity的主键id
     * @return 返回所有owner和当前activity的集合
     */
    Map getbyId(String id);

    /**
     * @param tblActivity 页面提交的修改数据
     */
    void editbyId(TblActivity tblActivity);

    /**
     * @param ids 要删除的记录的主键集合
     */
    void deleteById(List<String> ids);
}
