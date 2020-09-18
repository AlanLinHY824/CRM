package com.powernode.workbench.controller;

import com.powernode.util.Result;
import com.powernode.workbench.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @AlanLin 2020/8/29
 */
@RestController
@RequestMapping("chart")
public class ChartsController {

    @Autowired
    private ChartService chartService;

    @Value("${user_session}")
    private String USER_SESSION;

    @RequestMapping("tranChart")
    public Result tranChart(){
        Map<String, Object> list= chartService.getChartData();
        return Result.success(list);
    }

}
