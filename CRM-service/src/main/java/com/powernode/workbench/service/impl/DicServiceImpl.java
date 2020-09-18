package com.powernode.workbench.service.impl;

import com.powernode.workbench.mapper.TblDicTypeMapper;
import com.powernode.workbench.mapper.TblDicValueMapper;
import com.powernode.workbench.Comparator.DicValueComparator;
import com.powernode.workbench.pojo.TblDicType;
import com.powernode.workbench.pojo.TblDicValue;
import com.powernode.workbench.pojo.TblDicValueExample;
import com.powernode.workbench.service.DicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author AlanLin
 * @Description
 * @Date 2020/9/4
 */
@Service
public class DicServiceImpl implements DicService {

    @Autowired
    private TblDicTypeMapper tblDicTypeMapper;
    @Autowired
    private TblDicValueMapper tblDicValueMapper;

    @Override
    public Map<String, Set<TblDicValue>> getDicValue() {
        List<TblDicType> tblDicTypes = tblDicTypeMapper.selectByExample(null);
        Map<String, Set<TblDicValue>> tblDicValueMap=new HashMap<>();
        for (TblDicType tblDicType : tblDicTypes) {
            TblDicValueExample tblDicValueExample=new TblDicValueExample();
            TblDicValueExample.Criteria criteria = tblDicValueExample.createCriteria();
            criteria.andTypecodeEqualTo(tblDicType.getCode());
            List<TblDicValue> tblDicValues = tblDicValueMapper.selectByExample(tblDicValueExample);
            Set<TblDicValue> tblDicValueSet=new TreeSet<>(new DicValueComparator());
            tblDicValueSet.addAll(tblDicValues);
            tblDicValueMap.put(tblDicType.getCode(), tblDicValueSet);
        }

        return tblDicValueMap;
    }
}
