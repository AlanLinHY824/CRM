package com.powernode.listener;

import com.powernode.workbench.pojo.TblDicValue;
import com.powernode.workbench.service.DicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * @Author AlanLin
 * @Description
 * @Date 2020/9/4
 */
public class ApplicationListener implements ServletContextListener {

    @Autowired
    private DicService dicService;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext()).getAutowireCapableBeanFactory().autowireBean(this);
//        DicServiceImpl dicService = (DicServiceImpl)(WebApplicationContextUtils.getWebApplicationContext(event.getServletContext()).getBean("dicServiceImpl"));
        ServletContext application = event.getServletContext();
        ResourceBundle bundle = ResourceBundle.getBundle("properties/Stage2Possibility");
        Enumeration<String> keys = bundle.getKeys();
        Map<String, String> map=new TreeMap<>();
        while (keys.hasMoreElements()){
            String key = keys.nextElement();
            String value = bundle.getString(key);
            map.put(key,value);
        }
        application.setAttribute("poss", map);
        Map<String, Set<TblDicValue>> dicValue = dicService.getDicValue();
        application.setAttribute("stage",dicValue.get("stage"));
        application.setAttribute("appellation",dicValue.get("appellation"));
        application.setAttribute("clueState",dicValue.get("clueState"));
        application.setAttribute("returnPriority",dicValue.get("returnPriority"));
        application.setAttribute("source",dicValue.get("source"));
        application.setAttribute("stage",dicValue.get("stage"));
        application.setAttribute("transactionType",dicValue.get("transactionType"));
    }
}
