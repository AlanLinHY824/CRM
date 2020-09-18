package com.powernode.setting.service.impl;

import com.powernode.exception.ResultException;
import com.powernode.myenum.ResultEnum;
import com.powernode.setting.mapper.TblUserMapper;
import com.powernode.setting.pojo.TblUser;
import com.powernode.setting.pojo.TblUserExample;
import com.powernode.setting.service.UserService;
import com.powernode.util.DateTimeUtil;
import com.powernode.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Alan Lin
 * @AlanLin 2020/8/28
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TblUserMapper tblUserMapper;


    @Override
    public TblUser login(TblUser tblUser) {
        TblUserExample tblUserExample=new TblUserExample();
        TblUserExample.Criteria criteria = tblUserExample.createCriteria();
        criteria.andLoginactEqualTo(tblUser.getLoginact());
        List<TblUser> tblUsers = tblUserMapper.selectByExample(tblUserExample);
        if (tblUsers==null||tblUsers.size()==0){
            throw  new ResultException(ResultEnum.USER_NOT_FOUND);
        }
        criteria.andLoginpwdEqualTo(MD5Util.getMD5(tblUser.getLoginpwd()));
        List<TblUser> resTblUsers = tblUserMapper.selectByExample(tblUserExample);
        if (resTblUsers==null||resTblUsers.size()==0){
            throw new ResultException(ResultEnum.PWD_ERROR);
        }
        TblUser resTblUser = resTblUsers.get(0);
        if (!resTblUser.getAllowips().contains(tblUser.getAllowips())){
            throw new ResultException(ResultEnum.IP_lIMITED);
        }
        if (resTblUser.getExpiretime().compareTo(DateTimeUtil.getSysTime())<0){
            throw new ResultException(ResultEnum.ACC_EXPIRED);
        }
        if ("0".equals(resTblUser.getLockstate())){
            throw  new ResultException(ResultEnum.ACC_LOCKED);
        }
        return resTblUser;
    }

}
