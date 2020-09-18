package com.powernode.workbench.service;

import com.powernode.workbench.pojo.TblDicValue;

import java.util.Map;
import java.util.Set;

/**
 * @Author AlanLin
 * @Description
 * @Date 2020/9/4
 */
public interface DicService {
    Map<String, Set<TblDicValue>> getDicValue();
}
