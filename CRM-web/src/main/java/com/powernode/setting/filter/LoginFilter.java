package com.powernode.setting.filter;

import com.powernode.setting.pojo.TblUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @AlanLin 2020/8/29
 */
public class LoginFilter implements Filter {

    @Value("${user_session}")
    private String USER_SESSION;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext()).getAutowireCapableBeanFactory().autowireBean(this);
//        ResourceBundle bundle = ResourceBundle.getBundle("properties/constants");
//        USER_SESSION=bundle.getString("user_session");
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        if ("/crm/index.html".equals(requestURI)||requestURI.contains("login.html")||requestURI.contains("user/login")
                ||requestURI.endsWith("js") ||requestURI.endsWith("css")||requestURI.endsWith("png")||requestURI.contains("user/getcode") ||requestURI.endsWith("JPG")||requestURI.endsWith("gif")){
            filterChain.doFilter(request, response);
        }else {
            TblUser tblUser = (TblUser)(request.getSession().getAttribute(USER_SESSION));
            if (tblUser!=null){
                filterChain.doFilter(request, response);
            }else {
                response.sendRedirect("/crm/login.html");
            }
        }

    }

    @Override
    public void destroy() {

    }
}
