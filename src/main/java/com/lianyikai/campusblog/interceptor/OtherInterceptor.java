package com.lianyikai.campusblog.interceptor;

import com.lianyikai.campusblog.pojo.User;
import com.lianyikai.campusblog.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class OtherInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        return true;   
    }
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    	HttpSession session = httpServletRequest.getSession();
    	String contextPath=httpServletRequest.getServletContext().getContextPath();
        Subject subject = SecurityUtils.getSubject();
        if (subject != null && (subject.isRemembered() || subject.isAuthenticated())) {
            String name = subject.getPrincipal().toString();
            User user = userService.getByName(name);
            session.setAttribute("user", user);
            session.setAttribute("role", userService.getRoleByUser(user.getId()));
        }
    	httpServletRequest.getServletContext().setAttribute("contextPath", contextPath);
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}

