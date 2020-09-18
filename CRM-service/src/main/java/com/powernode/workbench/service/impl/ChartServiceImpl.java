package com.powernode.workbench.service.impl;

import com.powernode.workbench.mapper.TblTranHistoryMapper;
import com.powernode.workbench.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author AlanLin
 * @Description
 * @Date 2020/9/11
 */
@Service
public class ChartServiceImpl implements ChartService {

    @Autowired
    private TblTranHistoryMapper tblTranHistoryMapper;


    @Override
    public Map<String, Object> getChartData() {
        List<Map<String, Object>> chartData = tblTranHistoryMapper.getChartData();
        List<String> stageList=new ArrayList<>();
        List<Long> count=new ArrayList<>();
        for (Map<String, Object> chartDatum : chartData) {
                stageList.add((String) chartDatum.get("value"));
                count.add((Long)chartDatum.get("count"));
        }
        Map<String,Object> result=new HashMap<>();
        result.put("stage",stageList);
        result.put("stageCount",count);
        return result;
    }
}
