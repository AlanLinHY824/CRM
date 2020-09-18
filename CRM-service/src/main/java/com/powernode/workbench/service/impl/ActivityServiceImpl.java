package com.powernode.workbench.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.powernode.exception.ResultException;
import com.powernode.myenum.ResultEnum;
import com.powernode.setting.mapper.TblUserMapper;
import com.powernode.setting.pojo.TblUser;
import com.powernode.setting.pojo.TblUserExample;
import com.powernode.util.DateTimeUtil;
import com.powernode.util.JudgeUtils;
import com.powernode.util.PageResult;
import com.powernode.util.UUIDUtil;
import com.powernode.workbench.mapper.TblActivityMapper;
import com.powernode.workbench.pojo.TblActivity;
import com.powernode.workbench.pojo.TblActivityExample;
import com.powernode.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @AlanLin 2020/8/29
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private TblUserMapper tblUserMapper;

    @Autowired
    private TblActivityMapper tblActivityMapper;

    @Override
    public List<TblUser> getAllUsers() {
        List<TblUser> tblUsers = tblUserMapper.selectByExample(null);
        if (tblUsers==null||tblUsers.size()==0){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        return tblUsers;
    }

    @Override
    public void addActivity(TblActivity tblActivity) {
        if (!JudgeUtils.conditionJudge(tblActivity.getName())||!JudgeUtils.conditionJudge(tblActivity.getOwner())){
            throw new ResultException(ResultEnum.PARA_MISSING);
        }
        tblActivity.setCreatetime(DateTimeUtil.getSysTime());
        tblActivity.setId(UUIDUtil.getUUID());
        try {
            tblActivityMapper.insertSelective(tblActivity);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.Add_FAILED);
        }
    }

    @Override
    public PageResult<TblActivity> pageList(String name, String owner, String startdate, String enddate, int pageNo, int pageSize) {
        TblActivityExample activityExample=new TblActivityExample();
        TblActivityExample.Criteria activityCriteria = activityExample.createCriteria();
        if (JudgeUtils.conditionJudge(name)){
            activityCriteria.andNameLike("%"+name+"%");
        }

        if (JudgeUtils.conditionJudge(owner)){
            TblUserExample tblUserExample=new TblUserExample();
            TblUserExample.Criteria tblUserCriteria = tblUserExample.createCriteria();
            tblUserCriteria.andNameLike("%"+owner+"%");
            List<TblUser> tblUsers = tblUserMapper.selectByExample(tblUserExample);
            if (!JudgeUtils.collectionIsNull(tblUsers)){
                List<String> ids=new ArrayList<>();
                for (TblUser tblUser : tblUsers) {
                    ids.add(tblUser.getId());
                }
                activityCriteria.andOwnerIn(ids);
            }
        }
        if (JudgeUtils.conditionJudge(startdate)){
            activityCriteria.andStartdateGreaterThanOrEqualTo(startdate);
//            activityCriteria.andStartdateLike(startdate);
        }
        if (JudgeUtils.conditionJudge(enddate)){
//            activityCriteria.andStartdateLike(enddate);
            activityCriteria.andEnddateLessThanOrEqualTo(enddate);
        }
        PageHelper.startPage(pageNo, pageSize);
        List<TblActivity> tblActivities = tblActivityMapper.selectByExample(activityExample);
        if (JudgeUtils.collectionIsNull(tblActivities)){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }

        for (TblActivity tblActivity : tblActivities) {
            TblUser tblUser = tblUserMapper.selectByPrimaryKey(tblActivity.getOwner());
            tblActivity.setOwner(tblUser.getName());
        }

        Collections.sort(tblActivities, new Comparator<TblActivity>() {
            @Override
            public int compare(TblActivity o1, TblActivity o2) {
                return o2.getCreatetime().compareTo(o1.getCreatetime());
            }
        });
        PageInfo<TblActivity> pageInfo=new PageInfo<>(tblActivities);
        PageResult<TblActivity> pageResult=new PageResult<>();
        pageResult.setRows(pageInfo.getList());
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setPages(pageInfo.getPages());
        return pageResult;
    }

    @Override
    public Map getbyId(String id) {
        List<TblUser> tblUsers = tblUserMapper.selectByExample(null);
        if (JudgeUtils.collectionIsNull(tblUsers)){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        TblActivity tblActivity = tblActivityMapper.selectByPrimaryKey(id);
        if (tblActivity==null){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        Map map=new HashMap();
        map.put("users", tblUsers);
        map.put("act", tblActivity);
        return map;
    }

    @Override
    public void editbyId(TblActivity tblActivity) {
        if (!JudgeUtils.conditionJudge(tblActivity.getName())||!JudgeUtils.conditionJudge(tblActivity.getOwner())){
            throw new ResultException(ResultEnum.PARA_MISSING);
        }
        tblActivity.setEdittime(DateTimeUtil.getSysTime());
        try {
            tblActivityMapper.updateByPrimaryKeySelective(tblActivity);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void deleteById(List<String> ids) {
        TblActivityExample tblActivityExample=new TblActivityExample();
        TblActivityExample.Criteria activityExampleCriteria = tblActivityExample.createCriteria();
        activityExampleCriteria.andIdIn(ids);
        try {
            tblActivityMapper.deleteByExample(tblActivityExample);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.DELETE_FAILED);
        }
    }
}
