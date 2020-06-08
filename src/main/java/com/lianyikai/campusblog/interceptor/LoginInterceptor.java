package com.lianyikai.campusblog.interceptor;

import com.lianyikai.campusblog.pojo.Role;
import com.lianyikai.campusblog.pojo.User;
import com.lianyikai.campusblog.pojo.VisitRecord;
import com.lianyikai.campusblog.service.UserService;
import com.lianyikai.campusblog.service.VisitRecordService;
import com.lianyikai.campusblog.utils.CookieUtils;
import com.lianyikai.campusblog.utils.DateUtil;
import com.lianyikai.campusblog.utils.IpUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {
    public static Map<String, Date> VISIT_TIME = new HashMap<>();

	@Autowired
	UserService userService;
	@Autowired
    VisitRecordService visitRecordService;

	@Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String name = CookieUtils.getCookie(httpServletRequest, UserService.JSESSIONID);
		//获取IP地址
		String ipAddress = IpUtil.getIpAddr(httpServletRequest);

		HttpSession session = httpServletRequest.getSession();
        String contextPath = session.getServletContext().getContextPath();
        String[] requireAuthPages = new String[]{
        		"activity",
				"message",
				"home"
        };
		String[] requireSuperAdminPages = new String[]{
				"admin"
		};

        String uri = httpServletRequest.getRequestURI();
        uri = StringUtils.remove(uri, contextPath+"/");
        String page = uri;
        if (page.equals("error")) {
			httpServletResponse.sendRedirect("page404");
			return false;
		}
		Subject subject = SecurityUtils.getSubject();
        User user = null;
        if (subject.isRemembered() || subject.isAuthenticated()) {
            String userName = subject.getPrincipal().toString();
            user = userService.getByName(userName);
            visit(ipAddress, user);
        } else {
			visitor(name, ipAddress);
            visit(ipAddress, userService.getByName(name));
        }

		if(beginWith(page, requireAuthPages)){
			if(user == null) {
				httpServletResponse.sendRedirect("login");
				return false;
			}
		}
		if (beginWith(page, requireSuperAdminPages)){
			boolean isSuperAdmin = false;
			if(user != null) {
				int role = userService.getRoleByUser(user.getId());
				if (role == Role.SUPER_ASDMIN) {
					isSuperAdmin = true;
				}
			}
			if (!isSuperAdmin) {
				httpServletResponse.sendRedirect("login");
				return false;
			}
		}
        return true;   
    }

    private boolean beginWith(String page, String[] requiredAuthPages) {
    	boolean result = false;
    	for (String requiredAuthPage : requiredAuthPages) {
			if(StringUtils.startsWith(page, requiredAuthPage)) {
				result = true;	
				break;
			}
		}
    	return result;
	}

	private void visitor(String name, String ip) {
		if (name != null && name.length() > 0) {
			boolean exist = userService.isExist(name);
			if (!exist && (!VISIT_TIME.containsKey(ip) || DateUtil.overTime(VISIT_TIME.get(ip), new Date()) >= 1)) {
				User user = new User();
				user.setUsername(name);
				user.setPassword("visitor");
				user.setRegisterTime(new Date());
				user.setStatus(User.STATUS_NORMAL);
				user.setType(User.TYPE_VISITOR);
				userService.register(user);
			}
		}
	}

	private void visit(String ipAddress, User user) {
	    if (user == null)
	        return;
        VisitRecord record = new VisitRecord();
        record.setIp(ipAddress);
        record.setVisitor(user);
        record.setTime(new Date());
        if (!VISIT_TIME.containsKey(ipAddress) || DateUtil.overTime(VISIT_TIME.get(ipAddress), new Date()) >= 1) {
            visitRecordService.add(record);
            VISIT_TIME.put(ipAddress, new Date());
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
