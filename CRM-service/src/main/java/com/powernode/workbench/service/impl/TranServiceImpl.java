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
import com.powernode.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author AlanLin
 * @Description
 * @Date 2020/9/7
 */
@Service
public class TranServiceImpl implements TranService {

    @Autowired
    private TblUserMapper tblUserMapper;

    @Autowired
    private TblCustomerMapper tblCustomerMapper;

    @Autowired
    private TblContactsMapper tblContactsMapper;

    @Autowired
    private TblActivityMapper tblActivityMapper;

    @Autowired
    private TblTranMapper tblTranMapper;

    @Autowired
    private TblTranHistoryMapper tblTranHistoryMapper;

    @Autowired
    private TblContactsActivityRelationMapper tblContactsActivityRelationMapper;

    @Override
    public List<TblUser> getUsers() {
        List<TblUser> tblUsers = tblUserMapper.selectByExample(null);
        if (JudgeUtils.collectionIsNull(tblUsers)){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        return tblUsers;
    }

    @Override
    public List<Map<String,String>> getCustomers(String customerId, String name) {
        TblCustomerExample tblCustomerExample = new TblCustomerExample();
        TblCustomerExample.Criteria customerExampleCriteria = tblCustomerExample.createCriteria();
        if (JudgeUtils.conditionJudge(customerId)){
            customerExampleCriteria.andIdEqualTo(customerId);
        }
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
    public PageResult<TblContacts> getContacts(String customerName, String search, int pageNo, int pageSize) {
        TblCustomerExample tblCustomerExample = new TblCustomerExample();
        TblCustomerExample.Criteria customerExampleCriteria = tblCustomerExample.createCriteria();
        customerExampleCriteria.andNameEqualTo(customerName);
        List<TblCustomer> tblCustomers = tblCustomerMapper.selectByExample(tblCustomerExample);
        if(JudgeUtils.collectionIsNull(tblCustomers)){
            TblContactsExample tblContactsExample = new TblContactsExample();
            TblContactsExample.Criteria criteria = tblContactsExample.createCriteria();
            criteria.andCustomeridIsNull();
            criteria.andFullnameLike("%"+search+"%");

            PageHelper.startPage(pageNo, pageSize);
            List<TblContacts> tblContacts = tblContactsMapper.selectByExample(tblContactsExample);
            PageInfo<TblContacts> pageInfo=new PageInfo<>(tblContacts);
            PageResult<TblContacts> pageResult=new PageResult<>();
            pageResult.setRows(pageInfo.getList());
            pageResult.setPages(pageInfo.getPages());
            pageResult.setTotal(pageInfo.getTotal());
            return pageResult;
        }
        TblContactsExample tblContactsExample = new TblContactsExample();
        TblContactsExample.Criteria criteria = tblContactsExample.createCriteria();
        criteria.andCustomeridEqualTo(tblCustomers.get(0).getId());
        criteria.andFullnameLike("%"+search+"%");

        PageHelper.startPage(pageNo, pageSize);
        List<TblContacts> tblContacts = tblContactsMapper.selectByExample(tblContactsExample);
        PageInfo<TblContacts> pageInfo=new PageInfo<>(tblContacts);
        PageResult<TblContacts> pageResult=new PageResult<>();
        pageResult.setRows(pageInfo.getList());
        pageResult.setPages(pageInfo.getPages());
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

    @Transactional
    @Override
    public void createTran(TblTran tblTran) {
        if(!JudgeUtils.conditionJudge(tblTran.getCustomerid())){
            throw new ResultException(ResultEnum.CUSTOMER_MISSING);
        }
        if(!JudgeUtils.conditionJudge(tblTran.getOwner())){
            throw new ResultException(ResultEnum.OWNER_MISSING);
        }
        if(!JudgeUtils.conditionJudge(tblTran.getStage())){
            throw new ResultException(ResultEnum.STAGE_MISSING);
        }
        if(!JudgeUtils.conditionJudge(tblTran.getExpecteddate())){
            throw new ResultException(ResultEnum.EXPECTEDDATE_MISSING);
        }
        if(!JudgeUtils.conditionJudge(tblTran.getName())){
            throw new ResultException(ResultEnum.TRANNAME_MISSING);
        }
        String createtime = DateTimeUtil.getSysTime();
        TblCustomerExample tblCustomerExample = new TblCustomerExample();
        TblCustomerExample.Criteria customerExampleCriteria = tblCustomerExample.createCriteria();
        customerExampleCriteria.andNameEqualTo(tblTran.getCustomerid());
        List<TblCustomer> tblCustomers = tblCustomerMapper.selectByExample(tblCustomerExample);
        TblCustomer tblCustomer = null;
        //判断客户是否存在，不存在则创建
        if (JudgeUtils.collectionIsNull(tblCustomers)){
            tblCustomer = new TblCustomer();
            tblCustomer.setCreatetime(createtime);
            tblCustomer.setCreateby(tblTran.getCreateby());
            tblCustomer.setId(UUIDUtil.getUUID());
            tblCustomer.setOwner(tblTran.getOwner());
            tblCustomer.setName(tblTran.getCustomerid());
            tblCustomerMapper.insertSelective(tblCustomer);
            if (JudgeUtils.conditionJudge(tblTran.getContactsid())){
                //建立联系人与客户之间的关系
                TblContacts tblContacts = new TblContacts();
                tblContacts.setCustomerid(tblCustomer.getId());
                tblContacts.setCustomerid(tblTran.getContactsid());
                tblContacts.setEditby(tblTran.getCreateby());
                tblContacts.setEdittime(createtime);
                tblContactsMapper.updateByPrimaryKey(tblContacts);
            }
        }else {
            tblCustomer=tblCustomers.get(0);
        }
        //创建交易
        tblTran.setCustomerid(tblCustomer.getId());
        tblTran.setCreatetime(createtime);
        tblTran.setId(UUIDUtil.getUUID());
        tblTranMapper.insertSelective(tblTran);
        //建立联系人和市场活动的关系
        if (JudgeUtils.conditionJudge(tblTran.getContactsid())&&JudgeUtils.conditionJudge(tblTran.getActivityid())){
            TblContactsActivityRelationExample relationExample = new TblContactsActivityRelationExample();
            TblContactsActivityRelationExample.Criteria criteria = relationExample.createCriteria();
            criteria.andContactsidEqualTo(tblTran.getContactsid());
            criteria.andActivityidEqualTo(tblTran.getActivityid());
            List<TblContactsActivityRelation> tblContactsActivityRelations = tblContactsActivityRelationMapper.selectByExample(relationExample);

            if (JudgeUtils.collectionIsNull(tblContactsActivityRelations)){
                TblContactsActivityRelationExample relationExample1 = new TblContactsActivityRelationExample();
                TblContactsActivityRelation contactsActivityRelation = new TblContactsActivityRelation();
                contactsActivityRelation.setActivityid(tblTran.getActivityid());
                contactsActivityRelation.setContactsid(tblTran.getContactsid());
                contactsActivityRelation.setId(UUIDUtil.getUUID());
                tblContactsActivityRelationMapper.insertSelective(contactsActivityRelation);
            }

        }
        //创建交易历史
        TblTranHistory tblTranHistory = new TblTranHistory();
        tblTranHistory.setId(UUIDUtil.getUUID());
        tblTranHistory.setStage(tblTran.getStage());
        tblTranHistory.setMoney(tblTran.getMoney());
        tblTranHistory.setExpecteddate(tblTran.getExpecteddate());
        tblTranHistory.setCreatetime(tblTran.getCreatetime());
        tblTranHistory.setCreateby(tblTran.getCreateby());
        tblTranHistory.setTranid(tblTran.getId());
        tblTranHistoryMapper.insertSelective(tblTranHistory);

    }

    @Override
    public PageResult<TblTran> pageList(String fullname, String owner, String name, String customerName, String source, String stage, String type, int pageNo, int pageSize) {
        TblTranExample tblTranExample=new TblTranExample();
        TblTranExample.Criteria tblTranExampleCriteria = tblTranExample.createCriteria();
        if (JudgeUtils.conditionJudge(name)){
            tblTranExampleCriteria.andNameLike("%"+name+"%");
        }
        if (JudgeUtils.conditionJudge(source)){
            tblTranExampleCriteria.andSourceEqualTo(source);
        }
        if (JudgeUtils.conditionJudge(stage)){
            tblTranExampleCriteria.andStageEqualTo(stage);
        }
        if (JudgeUtils.conditionJudge(type)){
            tblTranExampleCriteria.andTypeEqualTo(type);
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
                tblTranExampleCriteria.andCustomeridIn(ids);
            }
        }

        if (JudgeUtils.conditionJudge(fullname)){
            TblContactsExample tblContactsExample = new TblContactsExample();
            TblContactsExample.Criteria contactsExampleCriteria = tblContactsExample.createCriteria();
            contactsExampleCriteria.andFullnameLike("%"+customerName+"%");
            List<TblContacts> tblContactsList = tblContactsMapper.selectByExample(tblContactsExample);
            if (!JudgeUtils.collectionIsNull(tblContactsList)){
                List<String> ids=new ArrayList<>();
                for (TblContacts tblContacts : tblContactsList) {
                    ids.add(tblContacts.getId());
                }
                tblTranExampleCriteria.andContactsidIn(ids);
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
                tblTranExampleCriteria.andOwnerIn(ids);
            }
        }
        PageHelper.startPage(pageNo, pageSize);
        List<TblTran> tblTranList = tblTranMapper.selectByExample(tblTranExample);
        if (!JudgeUtils.collectionIsNull(tblTranList)){
            for (TblTran tblTran : tblTranList) {
                TblUser tblUser = tblUserMapper.selectByPrimaryKey(tblTran.getOwner());
                TblCustomer tblCustomer = tblCustomerMapper.selectByPrimaryKey(tblTran.getCustomerid());
                TblContacts tblContacts = tblContactsMapper.selectByPrimaryKey(tblTran.getContactsid());
                tblTran.setOwner(tblUser.getName());
                tblTran.setCustomerid(tblCustomer.getName());
                tblTran.setContactsid(tblContacts.getFullname());
            }
        }
        PageInfo<TblTran> pageInfo=new PageInfo<>(tblTranList);
        PageResult<TblTran> pageResult=new PageResult<>();
        pageResult.setRows(pageInfo.getList());
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setPages(pageInfo.getPages());
        return pageResult;
    }

    @Override
    public Map getItem(String id) {
        Map map=new HashMap();
        List<TblUser> tblUsers = tblUserMapper.selectByExample(null);
        if (JudgeUtils.collectionIsNull(tblUsers)){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        map.put("users", tblUsers);
        TblTran tblTran = tblTranMapper.selectByPrimaryKey(id);
        if (tblTran==null){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        map.put("tblTran", tblTran);
        if (JudgeUtils.conditionJudge(tblTran.getContactsid())){
            TblContacts tblContacts = tblContactsMapper.selectByPrimaryKey(tblTran.getContactsid());
            if (tblContacts!=null){
                map.put("contact", tblContacts);
            }
        }
        if (JudgeUtils.conditionJudge(tblTran.getCustomerid())){
            TblCustomer tblCustomer = tblCustomerMapper.selectByPrimaryKey(tblTran.getCustomerid());
            if (tblCustomer!=null){
                map.put("tblCustomer", tblCustomer);
            }
        }
        if (JudgeUtils.conditionJudge(tblTran.getActivityid())){
            TblActivity tblActivity = tblActivityMapper.selectByPrimaryKey(tblTran.getActivityid());
            if (tblActivity!=null){
                map.put("act", tblActivity);
            }
        }
        return map;
    }

    @Transactional
    @Override
    public void editTran(TblTran tblTran) {
        tblTran.setEdittime(DateTimeUtil.getSysTime());
        TblTran oldTblTran = tblTranMapper.selectByPrimaryKey(tblTran.getId());
        try {
            tblTranMapper.updateByPrimaryKeySelective(tblTran);
            if (!oldTblTran.getStage().equals(tblTran.getStage())){
                TblTranHistory tblTranHistory = new TblTranHistory();
                tblTranHistory.setId(UUIDUtil.getUUID());
                tblTranHistory.setCreateby(tblTran.getEditby());
                tblTranHistory.setCreatetime(tblTran.getEdittime());
                tblTranHistory.setExpecteddate(tblTran.getExpecteddate());
                tblTranHistory.setMoney(tblTran.getMoney());
                tblTranHistory.setPoss(tblTran.getPoss());
                tblTranHistory.setTranid(tblTran.getId());
                tblTranHistory.setStage(tblTran.getStage());
                tblTranHistoryMapper.insertSelective(tblTranHistory);
            }
        } catch (Exception e) {
            throw new ResultException(ResultEnum.UPDATE_FAILED);
        }
    }

    /**
     * 1表示绿圈,2表示锚点,3表示黑圈,4表示红叉,5表示黑叉
     * @param tranId
     * @param stageSet
     * @param possMap
     * @return
     */
    @Override
    public Map<String, Object> stageIcon(String tranId, Set<TblDicValue> stageSet, Map<String, String> possMap) {
        TblTran tblTran = tblTranMapper.selectByPrimaryKey(tranId);
        List<TblDicValue> stageList =new ArrayList<>(stageSet);
        int currentindex=0;
        int index=0;
        for (int i = 0; i < stageList.size(); i++) {
            String poss = possMap.get(stageList.get(i).getValue());
            /*
              这里的可能性，也算是一种标识，把正常节点与失败节点，区分开，如果之后没有这个可能性，
              也需要考虑有没有字段可以将两类节点区分开，达到这样的效果，动态地将两个节点区分开，
              因为如果单纯通过下标来区分是不靠谱的，当节点发生数量变化时，代码就无法复用了
             */
            if ("0".equals(poss)){
                index=i;
                break;
            }
        }
        for (int i = 0; i < stageList.size(); i++) {
            if (tblTran.getStage().equals(stageList.get(i).getValue())){
                currentindex=i;
                break;
            }
        }
        List<Map<String,Object>> iconList=new ArrayList<>();
        if (currentindex<index){
            for (int i = 0; i < stageList.size(); i++) {
                Map<String,Object> map=new HashMap<>();
                if (i<currentindex){
                    map.put("iconNo", 1);
                    map.put("location", i);
                }else if (i==currentindex){
                    map.put("iconNo", 2);
                    map.put("location", i);
                }else if (i<index){
                    map.put("iconNo", 3);
                    map.put("location", i);
                }else {
                    map.put("iconNo", 5);
                    map.put("location", i);
                }
                iconList.add(i,map);
            }
        }else {
            for (int i = 0; i < stageList.size(); i++) {
                Map<String,Object> map=new HashMap<>();
                if (i<index){
                    map.put("iconNo", 3);
                    map.put("location", i);

                }else if (i==currentindex){
                    map.put("iconNo", 4);
                    map.put("location", i);

                }else {
                    map.put("iconNo", 5);
                    map.put("location", i);
                }
                iconList.add(i,map);
            }
        }
        Map<String,Object> result=new HashMap<>();
        result.put("iconList", iconList);
        result.put("index", index);
        result.put("stageList", stageList);
        return result;
    }

    @Override
    public TblTran getDetail(String id) {
        TblTran tblTran = tblTranMapper.selectByPrimaryKey(id);
        if (tblTran==null){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        TblUser tblUser = tblUserMapper.selectByPrimaryKey(tblTran.getOwner());
        if (tblUser==null){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        tblTran.setOwner(tblUser.getName());
        TblCustomer tblCustomer = tblCustomerMapper.selectByPrimaryKey(tblTran.getCustomerid());
        if (tblCustomer==null){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        tblTran.setCustomerid(tblCustomer.getName());
        TblContacts tblContacts = tblContactsMapper.selectByPrimaryKey(tblTran.getContactsid());
        if (tblCustomer!=null){
            tblTran.setContactsid(tblContacts.getFullname());
        }
        TblActivity tblActivity = tblActivityMapper.selectByPrimaryKey(tblTran.getActivityid());
        if (tblActivity!=null){
            tblTran.setActivityid(tblActivity.getName());
        }
        return tblTran;
    }

    @Transactional
    @Override
    public TblTran editStage(String tranId, String stage, String editby, Map<String, String> possMap) {
        String edittime = DateTimeUtil.getSysTime();
        TblTran tblTran = tblTranMapper.selectByPrimaryKey(tranId);
        tblTran.setEditby(editby);
        tblTran.setEdittime(edittime);
        tblTran.setStage(stage);
        tblTran.setPoss(possMap.get(stage));
        tblTranMapper.updateByPrimaryKeySelective(tblTran);

        TblTranHistory tblTranHistory = new TblTranHistory();
        tblTranHistory.setId(UUIDUtil.getUUID());
        tblTranHistory.setCreateby(editby);
        tblTranHistory.setCreatetime(edittime);
        tblTranHistory.setExpecteddate(tblTran.getExpecteddate());
        tblTranHistory.setMoney(tblTran.getMoney());
        tblTranHistory.setPoss(possMap.get(stage));
        tblTranHistory.setTranid(tblTran.getId());
        tblTranHistory.setStage(stage);
        tblTranHistoryMapper.insertSelective(tblTranHistory);

        return tblTran;

    }

    @Override
    public List<TblTranHistory> getHistory(String tranId, Map<String, String> possMap) {
        TblTranHistoryExample tblTranHistoryExample = new TblTranHistoryExample();
        TblTranHistoryExample.Criteria criteria = tblTranHistoryExample.createCriteria();
        criteria.andTranidEqualTo(tranId);
        List<TblTranHistory> tblTranHistories = tblTranHistoryMapper.selectByExample(tblTranHistoryExample);
        if (JudgeUtils.collectionIsNull(tblTranHistories)){
            throw new ResultException(ResultEnum.NOT_FOUND);
        }
        Collections.sort(tblTranHistories, new Comparator<TblTranHistory>() {
            @Override
            public int compare(TblTranHistory o1, TblTranHistory o2) {
                return o2.getCreatetime().compareTo(o1.getCreatetime());
            }
        });
        for (TblTranHistory tblTranHistory : tblTranHistories) {
            if (tblTranHistory.getStage()!=null){
                tblTranHistory.setPoss(possMap.get(tblTranHistory.getStage()));
            }
        }
        return tblTranHistories;
    }
}
