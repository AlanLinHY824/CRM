package com.powernode.workbench.service.impl;

import com.powernode.exception.ResultException;
import com.powernode.myenum.ResultEnum;
import com.powernode.util.DateTimeUtil;
import com.powernode.util.JudgeUtils;
import com.powernode.util.UUIDUtil;
import com.powernode.workbench.mapper.TblClueRemarkMapper;
import com.powernode.workbench.pojo.TblClueRemark;
import com.powernode.workbench.pojo.TblClueRemarkExample;
import com.powernode.workbench.service.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author AlanLin
 * @Description
 * @Date 2020/9/3
 */
@Service
public class ClueRemarkServiceImpl implements ClueRemarkService {

    @Autowired
    private TblClueRemarkMapper clueRemarkMapper;

    @Override
    public void addItem(TblClueRemark tblClueRemark) {
        tblClueRemark.setCreatetime(DateTimeUtil.getSysTime());
        tblClueRemark.setId(UUIDUtil.getUUID());
        tblClueRemark.setEditflag("0");
        try {
            clueRemarkMapper.insertSelective(tblClueRemark);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.Add_FAILED);
        }
    }

    @Override
    public List<TblClueRemark> getItems(String clueId) {
        TblClueRemarkExample tblClueRemarkExample=new TblClueRemarkExample();
        TblClueRemarkExample.Criteria clueRemarkExampleCriteria = tblClueRemarkExample.createCriteria();
        clueRemarkExampleCriteria.andClueidEqualTo(clueId);
        List<TblClueRemark> tblClueRemarks = clueRemarkMapper.selectByExample(tblClueRemarkExample);
        if (JudgeUtils.collectionIsNull(tblClueRemarks)){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        return tblClueRemarks;
    }

    @Override
    public void editItem(TblClueRemark tblClueRemark) {
        tblClueRemark.setEdittime(DateTimeUtil.getSysTime());
        tblClueRemark.setEditflag("1");
        try {
            clueRemarkMapper.updateByPrimaryKeySelective(tblClueRemark);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void deleteItem(String remarkId) {
        try {
            clueRemarkMapper.deleteByPrimaryKey(remarkId);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.DELETE_FAILED);
        }
    }
}
