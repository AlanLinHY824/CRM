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
import com.powernode.workbench.pojo.*;
import com.powernode.workbench.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author AlanLin
 * @Description
 * @Date 2020/9/9
 */
@Service
public class ContactsServiceImpl implements ContactsService {

    @Autowired
    private TblUserMapper tblUserMapper;

    @Autowired
    private TblContactsMapper tblContactsMapper;

    @Autowired
    private TblCustomerMapper tblCustomerMapper;

    @Override
    public PageResult<TblContacts> pageList(String fullname, String owner, String customerName, String source, String birth, int pageNo, int pageSize) {
        TblContactsExample tblContactsExample=new TblContactsExample();
        TblContactsExample.Criteria tblContactsExampleCriteria = tblContactsExample.createCriteria();
        if (JudgeUtils.conditionJudge(fullname)){
            tblContactsExampleCriteria.andFullnameLike("%"+fullname+"%");
        }
        if (JudgeUtils.conditionJudge(birth)){
            tblContactsExampleCriteria.andBirthLike("%"+birth+"%");
        }
        if (JudgeUtils.conditionJudge(source)){
            tblContactsExampleCriteria.andSourceLike("%"+source+"%");
        }

        if (JudgeUtils.conditionJudge(customerName)){
            TblCustomerExample tblCustomerExample = new TblCustomerExample();
            TblCustomerExample.Criteria customerExampleCriteria = tblCustomerExample.createCriteria();
            customerExampleCriteria.andNameLike("%"+customerName+"%");
            List<TblCustomer> tblCustomers = tblCustomerMapper.selectByExample(tblCustomerExample);
            if (!JudgeUtils.collectionIsNull(tblCustomers)){
                List<String> ids=new ArrayList<>();
                for (TblCustomer tblCustomer : tblCustomers) {
                    ids.add(tblCustomer.getId());
                }
                tblContactsExampleCriteria.andCustomeridIn(ids);
            }
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
                tblContactsExampleCriteria.andOwnerIn(ids);
            }
        }
        PageHelper.startPage(pageNo, pageSize);
        List<TblContacts> tblContactsList = tblContactsMapper.selectByExample(tblContactsExample);
        if (!JudgeUtils.collectionIsNull(tblContactsList)){
            for (TblContacts tblContacts : tblContactsList) {
                TblUser tblUser = tblUserMapper.selectByPrimaryKey(tblContacts.getOwner());
                TblCustomer tblCustomer = tblCustomerMapper.selectByPrimaryKey(tblContacts.getCustomerid());
                tblContacts.setOwner(tblUser.getName());
                tblContacts.setCustomerid(tblCustomer.getName());
            }
        }
        PageInfo<TblContacts> pageInfo=new PageInfo<>(tblContactsList);
        PageResult<TblContacts> pageResult=new PageResult<>();
        pageResult.setRows(pageInfo.getList());
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setPages(pageInfo.getPages());
        return pageResult;
    }

    @Override
    public List<TblUser> getAllUsers() {
        List<TblUser> tblUsers = tblUserMapper.selectByExample(null);
        if (JudgeUtils.collectionIsNull(tblUsers)){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        return tblUsers;
    }

    @Override
    public List<Map<String, String>> getCustomers(String name) {
        TblCustomerExample tblCustomerExample = new TblCustomerExample();
        TblCustomerExample.Criteria customerExampleCriteria = tblCustomerExample.createCriteria();
        if (JudgeUtils.conditionJudge(name)){
            customerExampleCriteria.andNameLike("%"+name+"%");
        }
        List<TblCustomer> tblCustomers = tblCustomerMapper.selectByExample(tblCustomerExample);
        List<Map<String,String>> mapList=new ArrayList<>();
        for (TblCustomer tblCustomer : tblCustomers) {
            Map<String,String> map=new HashMap<>();
            map.put("name", tblCustomer.getName());
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public Map getbyId(String id) {
        List<TblUser> tblUsers = tblUserMapper.selectByExample(null);
        if (JudgeUtils.collectionIsNull(tblUsers)){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        TblContacts tblContacts = tblContactsMapper.selectByPrimaryKey(id);
        if (tblContacts==null){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        TblCustomer tblCustomer = tblCustomerMapper.selectByPrimaryKey(tblContacts.getCustomerid());
        tblContacts.setCustomerid(tblCustomer.getName());
        Map map=new HashMap();
        map.put("users", tblUsers);
        map.put("contact", tblContacts);
        return map;
    }

    @Override
    public void editbyId(TblContacts tblContacts) {
        if (!JudgeUtils.conditionJudge(tblContacts.getFullname())){
            throw new ResultException(ResultEnum.CONTACT_MISSING);
        }
        if (!JudgeUtils.conditionJudge(tblContacts.getOwner())){
            throw new ResultException(ResultEnum.OWNER_MISSING);
        }
        tblContacts.setEdittime(DateTimeUtil.getSysTime());
        try {
            tblContactsMapper.updateByPrimaryKeySelective(tblContacts);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void deleteById(List<String> ids) {
        TblContactsExample tblContactsExample=new TblContactsExample();
        TblContactsExample.Criteria tblContactsExampleCriteria = tblContactsExample.createCriteria();
        tblContactsExampleCriteria.andIdIn(ids);
        try {
            tblContactsMapper.deleteByExample(tblContactsExample);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.DELETE_FAILED);
        }
    }

    @Override
    public PageResult<TblTran> getTran(String customerId, int pageNo, int pageSize) {
        return null;
    }

    @Override
    public PageResult<TblContacts> getContact(String customerId, int pageNo, int pageSize) {
        return null;
    }

    @Override
    public List<TblUser> getUsers() {
        return null;
    }

    @Override
    public void addContact(TblContacts tblContacts) {
        String createtime = DateTimeUtil.getSysTime();
        if (!JudgeUtils.conditionJudge(tblContacts.getFullname())){
            throw new ResultException(ResultEnum.CONTACT_MISSING);
        }
        if (!JudgeUtils.conditionJudge(tblContacts.getOwner())){
            throw new ResultException(ResultEnum.OWNER_MISSING);
        }
        if (JudgeUtils.conditionJudge(tblContacts.getCustomerid())){
            TblCustomerExample tblCustomerExample = new TblCustomerExample();
            TblCustomerExample.Criteria customerExampleCriteria = tblCustomerExample.createCriteria();
            customerExampleCriteria.andNameEqualTo(tblContacts.getCustomerid());
            List<TblCustomer> tblCustomers = tblCustomerMapper.selectByExample(tblCustomerExample);
            TblCustomer tblCustomer=null;
            if (JudgeUtils.collectionIsNull(tblCustomers)){
                tblCustomer = new TblCustomer();
                tblCustomer.setName(tblContacts.getCustomerid());
                tblCustomer.setId(UUIDUtil.getUUID());
                tblCustomer.setCreateby(tblContacts.getCreateby());
                tblCustomer.setCreatetime(createtime);
                tblCustomerMapper.insertSelective(tblCustomer);
            }else {
                tblCustomer=tblCustomers.get(0);
            }
            tblContacts.setCustomerid(tblCustomer.getId());
        }
        tblContacts.setCreatetime(createtime);
        tblContacts.setId(UUIDUtil.getUUID());
        try {
            tblContactsMapper.insertSelective(tblContacts);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.Add_FAILED);
        }
    }

    @Override
    public void deleteContact(String contactId) {

    }
}
