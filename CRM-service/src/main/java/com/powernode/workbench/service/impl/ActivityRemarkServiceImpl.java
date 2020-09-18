package com.powernode.workbench.service.impl;

import com.powernode.exception.ResultException;
import com.powernode.myenum.ResultEnum;
import com.powernode.util.DateTimeUtil;
import com.powernode.util.JudgeUtils;
import com.powernode.util.UUIDUtil;
import com.powernode.workbench.mapper.TblActivityRemarkMapper;
import com.powernode.workbench.pojo.TblActivityRemark;
import com.powernode.workbench.pojo.TblActivityRemarkExample;
import com.powernode.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Author AlanLin
 * @Description
 * @Date 2020/9/3
 */
@Service
public class ActivityRemarkServiceImpl implements ActivityRemarkService {

    @Autowired
    private TblActivityRemarkMapper remarkMapper;

    @Override
    public List<TblActivityRemark> getRemarksById(String activityId) {
        TblActivityRemarkExample activityRemarkExample =new TblActivityRemarkExample();
        TblActivityRemarkExample.Criteria remarkExampleCriteria = activityRemarkExample.createCriteria();
        remarkExampleCriteria.andActivityidEqualTo(activityId);
        List<TblActivityRemark> remarkList = remarkMapper.selectByExample(activityRemarkExample);
        if (JudgeUtils.collectionIsNull(remarkList)){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        Collections.sort(remarkList, new Comparator<TblActivityRemark>() {
            @Override
            public int compare(TblActivityRemark o1, TblActivityRemark o2) {
                String o1Time = o1.getEdittime() == null ? o1.getCreatetime() : o1.getEdittime();
                String o2Time = o2.getEdittime() == null ? o2.getCreatetime() : o2.getEdittime();
                return o2Time.compareTo(o1Time);
            }
        });
        return remarkMapper.selectByExample(activityRemarkExample);
    }

    @Override
    public void addItem(TblActivityRemark tblActivityRemark) {
        tblActivityRemark.setCreatetime(DateTimeUtil.getSysTime());
        tblActivityRemark.setEditflag("0");
        tblActivityRemark.setId(UUIDUtil.getUUID());
        try {
            remarkMapper.insertSelective(tblActivityRemark);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.Add_FAILED);
        }
    }

    @Override
    public void editItem(TblActivityRemark tblActivityRemark) {
        tblActivityRemark.setEdittime(DateTimeUtil.getSysTime());
        tblActivityRemark.setEditflag("1");
        try {
            remarkMapper.updateByPrimaryKeySelective(tblActivityRemark);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void delItem(String id) {
        try {
            remarkMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.DELETE_FAILED);
        }
    }
}
