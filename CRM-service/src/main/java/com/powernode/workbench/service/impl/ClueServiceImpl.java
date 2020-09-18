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
import com.powernode.workbench.mapper.*;
import com.powernode.workbench.pojo.*;
import com.powernode.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author AlanLin
 * @Description
 * @Date 2020/9/3
 */
@Service
public class ClueServiceImpl implements ClueService {
    @Autowired
    private TblClueMapper tblClueMapper;

    @Autowired
    private TblUserMapper tblUserMapper;

    @Autowired
    private TblActivityMapper tblActivityMapper;

    @Autowired
    private TblClueActivityRelationMapper tblClueActivityRelationMapper;

    @Autowired
    private TblContactsMapper tblContactsMapper;

    @Autowired
    private TblCustomerMapper tblCustomerMapper;

    @Autowired
    private TblContactsRemarkMapper tblContactsRemarkMapper;

    @Autowired
    private TblCustomerRemarkMapper tblCustomerRemarkMapper;

    @Autowired
    private TblTranMapper tblTranMapper;

    @Autowired
    private TblTranHistoryMapper tblTranHistoryMapper;

    @Autowired
    private TblContactsActivityRelationMapper tblContactsActivityRelationMapper;

    @Autowired
    private TblClueRemarkMapper tblClueRemarkMapper;


    @Override
    public PageResult<TblClue> pageList(String fullname, String company, String phone, String source, String owner, String mphone, String state, int pageNo, int pageSize) {
        TblClueExample tblClueExample=new TblClueExample();
        TblClueExample.Criteria clueExampleCriteria = tblClueExample.createCriteria();
        if (JudgeUtils.conditionJudge(owner)){
            TblUserExample tblUserExample=new TblUserExample();
            TblUserExample.Criteria tblUserCriteria = tblUserExample.createCriteria();
            tblUserCriteria.andNameLike("%"+owner+"%");
            List<TblUser> tblUsers = tblUserMapper.selectByExample(tblUserExample);
            if (JudgeUtils.collectionIsNull(tblUsers)){
                throw new ResultException(ResultEnum.NOT_FOUND);
            }
            List<String> ids=new ArrayList<>();
            for (TblUser tblUser : tblUsers) {
                ids.add(tblUser.getId());
            }
            clueExampleCriteria.andOwnerIn(ids);
        }
        if (JudgeUtils.conditionJudge(fullname)){
            clueExampleCriteria.andFullnameLike("%"+fullname+"%");
        }
        if (JudgeUtils.conditionJudge(company)){
            clueExampleCriteria.andCompanyLike("%"+company+"%");
        }
        if (JudgeUtils.conditionJudge(phone)){
            clueExampleCriteria.andPhoneLike("%"+phone+"%");
        }
        if (JudgeUtils.conditionJudge(source)){
            clueExampleCriteria.andSourceEqualTo(source);
        }
        if (JudgeUtils.conditionJudge(mphone)){
            clueExampleCriteria.andMphoneLike("%"+mphone+"%");
        }
        if (JudgeUtils.conditionJudge(state)) {
            clueExampleCriteria.andStateEqualTo(state);
        }
        PageHelper.startPage(pageNo,pageSize);
        List<TblClue> tblClues = tblClueMapper.selectByExample(tblClueExample);
        if (JudgeUtils.collectionIsNull(tblClues)){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        for (TblClue tblClue : tblClues) {
            TblUser tblUser = tblUserMapper.selectByPrimaryKey(tblClue.getOwner());
            tblClue.setOwner(tblUser.getName());
        }
        PageInfo<TblClue> pageInfo=new PageInfo<>(tblClues);
        PageResult<TblClue> pageResult=new PageResult<>();
        pageResult.setPages(pageInfo.getPages());
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setRows(pageInfo.getList());
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
    public void addItem(TblClue tblClue) {
        if (!JudgeUtils.conditionJudge(tblClue.getCompany())||!JudgeUtils.conditionJudge(tblClue.getFullname())||!JudgeUtils.conditionJudge(tblClue.getOwner())){
            throw new ResultException(ResultEnum.PARA_MISSING);
        }
        tblClue.setId(UUIDUtil.getUUID());
        tblClue.setCreatetime(DateTimeUtil.getSysTime());
        try {
            tblClueMapper.insertSelective(tblClue);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.Add_FAILED);
        }
    }

    @Override
    public Map<String,Object> getItem(String id) {
        TblClue tblClue = tblClueMapper.selectByPrimaryKey(id);
        if (tblClue==null){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        List<TblUser> tblUsers = tblUserMapper.selectByExample(null);
        if (JudgeUtils.collectionIsNull(tblUsers)){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        Map<String,Object> objectMap=new HashMap<>();
        objectMap.put("users", tblUsers);
        objectMap.put("clue", tblClue);
        return objectMap;
    }

    @Override
    public void editItem(TblClue tblClue) {
        if (!JudgeUtils.conditionJudge(tblClue.getCompany())||!JudgeUtils.conditionJudge(tblClue.getFullname())||!JudgeUtils.conditionJudge(tblClue.getOwner())){
            throw new ResultException(ResultEnum.PARA_MISSING);
        }
        tblClue.setEdittime(DateTimeUtil.getSysTime());
        try {
            tblClueMapper.updateByPrimaryKeySelective(tblClue);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void delItem(List<String> ids) {
        TblClueExample tblClueExample=new TblClueExample();
        TblClueExample.Criteria clueExampleCriteria = tblClueExample.createCriteria();
        clueExampleCriteria.andIdIn(ids);
        try {
            tblClueMapper.deleteByExample(tblClueExample);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.DELETE_FAILED);
        }
    }

    @Override
    public TblClue getDetail(String clueId) {
        TblClue tblClue = tblClueMapper.selectByPrimaryKey(clueId);
        if (tblClue==null){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        TblUser tblUser = tblUserMapper.selectByPrimaryKey(tblClue.getOwner());
        if (tblUser==null){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        tblClue.setOwner(tblUser.getName());
        return tblClue;
    }

    @Override
    public PageResult<TblActivity> getRelation(String search, String clueId, Integer pageNo, Integer pageSize) {
        TblClueActivityRelationExample tblClueActivityRelationExample=new TblClueActivityRelationExample();
        TblClueActivityRelationExample.Criteria relationExampleCriteria = tblClueActivityRelationExample.createCriteria();
        relationExampleCriteria.andClueidEqualTo(clueId);
        List<TblClueActivityRelation> tblClueActivityRelations = tblClueActivityRelationMapper.selectByExample(tblClueActivityRelationExample);
        if (JudgeUtils.collectionIsNull(tblClueActivityRelations)){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }

        List<String> activityIds=new ArrayList<>();
        TblActivityExample activityExample=new TblActivityExample();
        for (TblClueActivityRelation tblClueActivityRelation : tblClueActivityRelations) {
            activityIds.add(tblClueActivityRelation.getActivityid());
            TblActivityExample.Criteria activityExampleCriteria = activityExample.createCriteria();
            activityExampleCriteria.andIdIn(activityIds);
            if (JudgeUtils.conditionJudge(search)){
                activityExampleCriteria.andNameLike("%"+search+"%");
            }
        }
        PageHelper.startPage(pageNo,pageSize);
        List<TblActivity> tblActivities = tblActivityMapper.selectByExample(activityExample);

        if (JudgeUtils.collectionIsNull(tblActivities)){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        for (TblActivity tblActivity : tblActivities) {
            TblUser tblUser = tblUserMapper.selectByPrimaryKey(tblActivity.getOwner());
            tblActivity.setOwner(tblUser.getName());
        }
        PageInfo<TblActivity> pageInfo=new PageInfo<>(tblActivities);
        PageResult<TblActivity> pageResult=new PageResult<>();
        pageResult.setPages(pageInfo.getPages());
        pageResult.setRows(pageInfo.getList());
        pageResult.setTotal(pageInfo.getTotal());
        return pageResult;
    }

    @Override
    public PageResult<TblActivity> getActivities(String search, Integer pageNo, Integer pageSize) {
        TblActivityExample activityExample=new TblActivityExample();
        TblActivityExample.Criteria activityExampleCriteria = activityExample.createCriteria();
        if (JudgeUtils.conditionJudge(search)){
            activityExampleCriteria.andNameLike("%"+search+"%");
        }
        PageHelper.startPage(pageNo,pageSize);
        List<TblActivity> tblActivities = tblActivityMapper.selectByExample(activityExample);
        if (JudgeUtils.collectionIsNull(tblActivities)){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        for (TblActivity tblActivity : tblActivities) {
            TblUser tblUser = tblUserMapper.selectByPrimaryKey(tblActivity.getOwner());
            tblActivity.setOwner(tblUser.getName());
        }
        PageInfo<TblActivity> pageInfo=new PageInfo<>(tblActivities);
        PageResult<TblActivity> pageResult=new PageResult<>();
        pageResult.setPages(pageInfo.getPages());
        pageResult.setRows(pageInfo.getList());
        pageResult.setTotal(pageInfo.getTotal());
        return pageResult;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void addRelation(List<String> actIds, String clueId) {
        TblClueActivityRelation clueActivityRelation=new TblClueActivityRelation();
        clueActivityRelation.setClueid(clueId);
        for (String actId : actIds) {
            clueActivityRelation.setActivityid(actId);
            clueActivityRelation.setId(UUIDUtil.getUUID());
            try {
                tblClueActivityRelationMapper.insert(clueActivityRelation);
            } catch (Exception e) {
                if (e instanceof DuplicateKeyException){
                    throw new ResultException(ResultEnum.RELATION_EXIST);
                }
                throw new ResultException(ResultEnum.Add_FAILED);
            }
        }

    }

    @Override
    public void delRelation(String actId, String clueId) {
        TblClueActivityRelationExample relationExample=new TblClueActivityRelationExample();
        TblClueActivityRelationExample.Criteria criteria = relationExample.createCriteria();
        criteria.andClueidEqualTo(clueId);
        criteria.andActivityidEqualTo(actId);
        try {
            tblClueActivityRelationMapper.deleteByExample(relationExample);
        } catch (Exception e) {
            throw new ResultException(ResultEnum.DELETE_FAILED);
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void transferClue(TblTran tblTran, String createby, String clueId, String flag) {
        String createtime=DateTimeUtil.getSysTime();

        TblClue tblClue = tblClueMapper.selectByPrimaryKey(clueId);
        if (!JudgeUtils.conditionJudge(tblClue)){
            throw new ResultException(ResultEnum.CLUE_NOT_FOUND);
        }
        TblCustomerExample tblCustomerExample=new TblCustomerExample();
        TblCustomerExample.Criteria tblCustomerExampleCriteria = tblCustomerExample.createCriteria();
        tblCustomerExampleCriteria.andNameEqualTo(tblClue.getCompany());
        List<TblCustomer> tblCustomers = tblCustomerMapper.selectByExample(tblCustomerExample);
        TblCustomer tblCustomer=null;
        //如果没有该客户，创建一个客户
        if (JudgeUtils.collectionIsNull(tblCustomers)){
            tblCustomer=new TblCustomer();
            tblCustomer.setId(UUIDUtil.getUUID());
            tblCustomer.setOwner(tblClue.getOwner());
            tblCustomer.setName(tblClue.getCompany());
            tblCustomer.setWebsite(tblClue.getWebsite());
            tblCustomer.setPhone(tblClue.getPhone());
            tblCustomer.setCreateby(createby);
            tblCustomer.setCreatetime(createtime);
            tblCustomer.setContactsummary(tblClue.getContactsummary());
            tblCustomer.setNextcontacttime(tblClue.getNextcontacttime());
            tblCustomer.setDescription(tblClue.getDescription());
            tblCustomer.setAddress(tblClue.getAddress());
            tblCustomerMapper.insertSelective(tblCustomer);
        }else {
            tblCustomer=tblCustomers.get(0);
        }
        //创建联系人
        TblContacts tblContacts=new TblContacts();
        tblContacts.setId(UUIDUtil.getUUID());
        tblContacts.setOwner(tblClue.getOwner());
        tblContacts.setSource(tblClue.getSource());
        tblContacts.setCustomerid(tblCustomer.getId());
        tblContacts.setFullname(tblClue.getFullname());
        tblContacts.setAppellation(tblClue.getAppellation());
        tblContacts.setEmail(tblClue.getEmail());
        tblContacts.setMphone(tblClue.getMphone());
        tblContacts.setJob(tblClue.getJob());
        tblContacts.setCreateby(createby);
        tblContacts.setCreatetime(createtime);
        tblContacts.setDescription(tblClue.getDescription());
        tblContacts.setContactsummary(tblClue.getContactsummary());
        tblContacts.setNextcontacttime(tblClue.getNextcontacttime());
        tblContacts.setAddress(tblClue.getAddress());
        tblContactsMapper.insertSelective(tblContacts);
        //创建联系人和市场活动的关系
        TblClueActivityRelationExample clueActivityRelationExample=new TblClueActivityRelationExample();
        TblClueActivityRelationExample.Criteria clueActivityRelationExampleCriteria = clueActivityRelationExample.createCriteria();
        clueActivityRelationExampleCriteria.andClueidEqualTo(clueId);
        List<TblClueActivityRelation> tblClueActivityRelations = tblClueActivityRelationMapper.selectByExample(clueActivityRelationExample);
        for (TblClueActivityRelation tblClueActivityRelation : tblClueActivityRelations) {
            TblContactsActivityRelation contactsActivityRelation=new TblContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsid(tblContacts.getId());
            contactsActivityRelation.setActivityid(tblClueActivityRelation.getActivityid());
            tblContactsActivityRelationMapper.insert(contactsActivityRelation);
        }
        //将备注转移至联系人、客户和交易中（如有）
        TblClueRemarkExample tblClueRemarkExample=new TblClueRemarkExample();
        TblClueRemarkExample.Criteria clueRemarkExampleCriteria = tblClueRemarkExample.createCriteria();
        clueRemarkExampleCriteria.andClueidEqualTo(clueId);
        List<TblClueRemark> tblClueRemarks = tblClueRemarkMapper.selectByExample(tblClueRemarkExample);
        if (!JudgeUtils.collectionIsNull(tblClueRemarks)){
            for (TblClueRemark tblClueRemark : tblClueRemarks) {
                //联系人备注
                TblContactsRemark tblContactsRemark = new TblContactsRemark();
                tblContactsRemark.setId(UUIDUtil.getUUID());
                tblContactsRemark.setNotecontent(tblClueRemark.getNotecontent());
                tblContactsRemark.setCreateby(tblClueRemark.getCreateby());
                tblContactsRemark.setCreatetime(tblClueRemark.getCreatetime());
                tblContactsRemark.setEditby(tblClueRemark.getEditby());
                tblContactsRemark.setEdittime(tblClueRemark.getEdittime());
                tblContactsRemark.setEditflag(tblClueRemark.getEditflag());
                tblContactsRemark.setContactsid(tblContacts.getId());
                tblContactsRemarkMapper.insertSelective(tblContactsRemark);
                //客户备注
                TblCustomerRemark tblCustomerRemark = new TblCustomerRemark();
                tblCustomerRemark.setId(UUIDUtil.getUUID());
                tblCustomerRemark.setNotecontent(tblClueRemark.getNotecontent());
                tblCustomerRemark.setCreateby(tblClueRemark.getCreateby());
                tblCustomerRemark.setCreatetime(tblClueRemark.getCreatetime());
                tblCustomerRemark.setEditby(tblClueRemark.getEditby());
                tblCustomerRemark.setEdittime(tblClueRemark.getEdittime());
                tblCustomerRemark.setEditflag(tblClueRemark.getEditflag());
                tblCustomerRemark.setCustomerid(tblCustomer.getId());
                tblCustomerRemarkMapper.insertSelective(tblCustomerRemark);
            }
        }

        if ("1".equals(flag)){
            if(!JudgeUtils.conditionJudge(tblTran.getMoney())||!JudgeUtils.conditionJudge(tblTran.getActivityid())||!JudgeUtils.conditionJudge(tblTran.getName())||!JudgeUtils.conditionJudge(tblTran.getExpecteddate())||!JudgeUtils.conditionJudge(tblTran.getStage())){
                throw new ResultException(ResultEnum.PARA_MISSING);
            }
            //创建交易
            tblTran.setCreateby(createby);
            tblTran.setCreatetime(createtime);
            tblTran.setCustomerid(tblCustomer.getId());
            tblTran.setDescription(tblClue.getDescription());
            tblTran.setId(UUIDUtil.getUUID());
            tblTran.setNextcontacttime(tblClue.getNextcontacttime());
            tblTran.setOwner(tblClue.getOwner());
            tblTran.setSource(tblClue.getSource());
            tblTran.setContactsid(tblContacts.getId());
            tblTranMapper.insertSelective(tblTran);
            //交易历史
            TblTranHistory tranHistory=new TblTranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setStage(tblTran.getStage());
            tranHistory.setMoney(tblTran.getMoney());
            tranHistory.setExpecteddate(tblTran.getExpecteddate());
            tranHistory.setCreatetime(createtime);
            tranHistory.setCreateby(createby);
            tranHistory.setTranid(tblTran.getId());
            tblTranHistoryMapper.insertSelective(tranHistory);
        }
        //删除线索和活动的关系
        TblClueActivityRelationExample tblClueActivityRelationExample=new TblClueActivityRelationExample();
        TblClueActivityRelationExample.Criteria clueActivityRelationExampleCriteria1 = tblClueActivityRelationExample.createCriteria();
        clueActivityRelationExampleCriteria1.andClueidEqualTo(clueId);
        tblClueActivityRelationMapper.deleteByExample(tblClueActivityRelationExample);

        //删除线索备注
        TblClueRemarkExample tblClueRemarkExample1 = new TblClueRemarkExample();
        TblClueRemarkExample.Criteria criteria = tblClueRemarkExample1.createCriteria();
        criteria.andClueidEqualTo(clueId);
        tblClueRemarkMapper.deleteByExample(tblClueRemarkExample1);

        //删除线索
        tblClueMapper.deleteByPrimaryKey(clueId);

    }
}
