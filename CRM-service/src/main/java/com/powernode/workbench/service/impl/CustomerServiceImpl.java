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
import com.powernode.workbench.mapper.TblContactsMapper;
import com.powernode.workbench.mapper.TblCustomerMapper;
import com.powernode.workbench.mapper.TblTranMapper;
import com.powernode.workbench.pojo.*;
import com.powernode.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author AlanLin
 * @Description
 * @Date 2020/9/7
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private TblCustomerMapper tblCustomerMapper;

    @Autowired
    private TblUserMapper tblUserMapper;

    @Autowired
    private TblTranMapper tblTranMapper;

    @Autowired
    private TblContactsMapper tblContactsMapper;

    @Override
    public PageResult<TblCustomer> pageList(String name, String owner, String phone, String website, int pageNo, int pageSize) {
        TblCustomerExample tblCustomerExample=new TblCustomerExample();
        TblCustomerExample.Criteria customerExampleCriteria = tblCustomerExample.createCriteria();
        if (JudgeUtils.conditionJudge(name)){
            customerExampleCriteria.andNameLike("%"+name+"%");
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
                customerExampleCriteria.andOwnerIn(ids);
            }
        }
        if (JudgeUtils.conditionJudge(phone)){
            customerExampleCriteria.andPhoneEqualTo(phone);
//            activityCriteria.andStartdateLike(startdate);
        }
        if (JudgeUtils.conditionJudge(website)){
//            activityCriteria.andStartdateLike(enddate);
            customerExampleCriteria.andWebsiteLike("%"+website+"%");
        }
        PageHelper.startPage(pageNo, pageSize);
        List<TblCustomer> tblCustomers = tblCustomerMapper.selectByExample(tblCustomerExample);
        if (!JudgeUtils.collectionIsNull(tblCustomers)){
            for (TblCustomer tblCustomer : tblCustomers) {
                TblUser tblUser = tblUserMapper.selectByPrimaryKey(tblCustomer.getOwner());
                tblCustomer.setOwner(tblUser.getName());
            }
        }
        PageInfo<TblCustomer> pageInfo=new PageInfo<>(tblCustomers);
        PageResult<TblCustomer> pageResult=new PageResult<>();
        pageResult.setRows(pageInfo.getList());
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setPages(pageInfo.getPages());
        return pageResult;
    }

    @Override
    public List<TblUser> getAllUsers() {
        List<TblUser> tblUsers = tblUserMapper.selectByExample(null);
        if (tblUsers==null||tblUsers.size()==0){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        return tblUsers;
    }

    @Override
    public void addCustomer(TblCustomer tblCustomer) {
        if (!JudgeUtils.conditionJudge(tblCustomer.getName())||!JudgeUtils.conditionJudge(tblCustomer.getOwner())){
            throw new ResultException(ResultEnum.PARA_MISSING);
        }
        tblCustomer.setCreatetime(DateTimeUtil.getSysTime());
        tblCustomer.setId(UUIDUtil.getUUID());
        try {
            tblCustomerMapper.insertSelective(tblCustomer);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.Add_FAILED);
        }
    }

    @Override
    public Map getbyId(String id) {
        List<TblUser> tblUsers = tblUserMapper.selectByExample(null);
        if (JudgeUtils.collectionIsNull(tblUsers)){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        TblCustomer tblCustomer = tblCustomerMapper.selectByPrimaryKey(id);
        if (tblCustomer==null){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        Map map=new HashMap();
        map.put("users", tblUsers);
        map.put("customer", tblCustomer);
        return map;
    }

    @Override
    public void editbyId(TblCustomer tblCustomer) {
        if (!JudgeUtils.conditionJudge(tblCustomer.getName())||!JudgeUtils.conditionJudge(tblCustomer.getOwner())){
            throw new ResultException(ResultEnum.PARA_MISSING);
        }
        tblCustomer.setEdittime(DateTimeUtil.getSysTime());
        try {
            tblCustomerMapper.updateByPrimaryKeySelective(tblCustomer);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void deleteById(List<String> ids) {
        TblCustomerExample tblCustomerExample=new TblCustomerExample();
        TblCustomerExample.Criteria customerExampleCriteria = tblCustomerExample.createCriteria();
        customerExampleCriteria.andIdIn(ids);
        try {
            tblCustomerMapper.deleteByExample(tblCustomerExample);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.DELETE_FAILED);
        }
    }

    @Override
    public PageResult<TblTran> getTran(String customerId, int pageNo, int pageSize) {
        TblTranExample tblTranExample=new TblTranExample();
        TblTranExample.Criteria tblTranExampleCriteria = tblTranExample.createCriteria();
        tblTranExampleCriteria.andCustomeridEqualTo(customerId);
        PageHelper.startPage(pageNo,pageSize);
        List<TblTran> tblTranList = tblTranMapper.selectByExample(tblTranExample);
        if (JudgeUtils.collectionIsNull(tblTranList)){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }

        PageInfo<TblTran> pageInfo=new PageInfo<>(tblTranList);
        PageResult<TblTran> pageResult=new PageResult<>();
        pageResult.setPages(pageInfo.getPages());
        pageResult.setRows(pageInfo.getList());
        pageResult.setTotal(pageInfo.getTotal());
        return pageResult;
    }

    @Override
    public PageResult<TblContacts> getContact(String customerId, int pageNo, int pageSize) {
        TblContactsExample tblContactsExample=new TblContactsExample();
        TblContactsExample.Criteria tblContactsExampleCriteria = tblContactsExample.createCriteria();
        tblContactsExampleCriteria.andCustomeridEqualTo(customerId);
        PageHelper.startPage(pageNo,pageSize);
        List<TblContacts> tblContactsList = tblContactsMapper.selectByExample(tblContactsExample);
        if (JudgeUtils.collectionIsNull(tblContactsList)){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }

        PageInfo<TblContacts> pageInfo=new PageInfo<>(tblContactsList);
        PageResult<TblContacts> pageResult=new PageResult<>();
        pageResult.setPages(pageInfo.getPages());
        pageResult.setRows(pageInfo.getList());
        pageResult.setTotal(pageInfo.getTotal());
        return pageResult;
    }

    @Override
    public List<TblUser> getUsers() {
        List<TblUser> tblUsers = tblUserMapper.selectByExample(null);
        if (JudgeUtils.collectionIsNull(tblUsers)){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        return tblUsers;
    }

    @Override
    public void addContact(TblContacts tblContacts) {
        if (!JudgeUtils.conditionJudge(tblContacts.getFullname())||!JudgeUtils.conditionJudge(tblContacts.getOwner())){
            throw new ResultException(ResultEnum.PARA_MISSING);
        }
        tblContacts.setCreatetime(DateTimeUtil.getSysTime());
        tblContacts.setId(UUIDUtil.getUUID());
        try {
            tblContactsMapper.insertSelective(tblContacts);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.Add_FAILED);
        }
    }

    @Override
    public void deleteContact(String contactId) {
        try {
            tblContactsMapper.deleteByPrimaryKey(contactId);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.Add_FAILED);
        }
    }
}
