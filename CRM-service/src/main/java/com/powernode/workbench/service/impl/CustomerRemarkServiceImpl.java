package com.powernode.workbench.service.impl;

import com.powernode.exception.ResultException;
import com.powernode.myenum.ResultEnum;
import com.powernode.util.DateTimeUtil;
import com.powernode.util.JudgeUtils;
import com.powernode.util.UUIDUtil;
import com.powernode.workbench.mapper.TblCustomerRemarkMapper;
import com.powernode.workbench.pojo.TblCustomerRemark;
import com.powernode.workbench.pojo.TblCustomerRemarkExample;
import com.powernode.workbench.service.CustomerRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author AlanLin
 * @Description
 * @Date 2020/9/8
 */
@Service
public class CustomerRemarkServiceImpl implements CustomerRemarkService {

    @Autowired
    TblCustomerRemarkMapper tblCustomerRemarkMapper;

    @Override
    public List<TblCustomerRemark> getItems(String customerId) {
        TblCustomerRemarkExample tblClueRemarkExample=new TblCustomerRemarkExample();
        TblCustomerRemarkExample.Criteria tblClueRemarkExampleCriteria = tblClueRemarkExample.createCriteria();
        tblClueRemarkExampleCriteria.andCustomeridEqualTo(customerId);
        List<TblCustomerRemark> tblCustomerRemarkList = tblCustomerRemarkMapper.selectByExample(tblClueRemarkExample);
        if (JudgeUtils.collectionIsNull(tblCustomerRemarkList)){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        return tblCustomerRemarkList;
    }

    @Override
    public void addItem(TblCustomerRemark tblCustomerRemark) {
        tblCustomerRemark.setCreatetime(DateTimeUtil.getSysTime());
        tblCustomerRemark.setId(UUIDUtil.getUUID());
        tblCustomerRemark.setEditflag("0");
        try {
            tblCustomerRemarkMapper.insertSelective(tblCustomerRemark);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.Add_FAILED);
        }
    }

    @Override
    public void editItem(TblCustomerRemark tblCustomerRemark) {
        tblCustomerRemark.setEdittime(DateTimeUtil.getSysTime());
        tblCustomerRemark.setEditflag("1");
        try {
            tblCustomerRemarkMapper.updateByPrimaryKeySelective(tblCustomerRemark);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void deleteItem(String remarkId) {
        try {
            tblCustomerRemarkMapper.deleteByPrimaryKey(remarkId);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.DELETE_FAILED);
        }
    }

}
