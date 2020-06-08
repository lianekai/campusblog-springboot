package com.lianyikai.campusblog.service;

import com.lianyikai.campusblog.dao.RoleDao;
import com.lianyikai.campusblog.dao.UserDao;
import com.lianyikai.campusblog.pojo.Role;
import com.lianyikai.campusblog.pojo.User;
import com.lianyikai.campusblog.utils.Page4Navigator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@CacheConfig(cacheNames="users")
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    public static final String JSESSIONID = "JSESSIONID";

    @Autowired
    UserDao userDao;
    @Autowired
    RoleDao roleDao;

    /*
     * 用户数量
     * */
    @Cacheable(key="'all-users-count'")
    public long countAll() {
        return userDao.count();
    }

    /*
     * 用户列表分页
     * */
    @Cacheable(key="'users-page-'+#p0+'-'+#p1")
    public Page4Navigator<User> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "registerTime");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<User> pageFromJPA = userDao.findAll(pageable);
        setUserRole(pageFromJPA.getContent());
        return new Page4Navigator<>(start + 1, pageFromJPA, navigatePages);
    }

    /*
     * 注册
     * */
    @Caching(evict = {@CacheEvict(value = "users", allEntries = true),
            @CacheEvict(value = "comments", allEntries = true)
    })
    public void save(User bean) {
        userDao.save(bean);
    }

    /*
     * 注册
     * */
    @CacheEvict(allEntries=true)
    public void register(User bean) {
        // 同步锁，拦截器多次异步执行，抛出duplicate key username-index异常
        synchronized (this) {
            if (!isExist(bean.getUsername())) {
                User user = userDao.save(bean);
                roleDao.addUserRole(user.getId(), Role.USER);
            }
        }
    }

    /*
    * 用户是否存在
    * */
    @Cacheable(key="'is-exist-'+#p0")
    public boolean isExist(String name) {
//        UserService userService = SpringContextUtil.getBean(UserService.class);
        User user = getByName(name);
        return null!=user && user.getId() > 0;
    }

    /*
     * 用户角色是否存在
     * */
    @Cacheable(key="'is-role-exist-'+#p0")
    public boolean isRoleExist(int uid) {
        try {
            return roleDao.isExist(uid) > 0;
        } catch (Exception ignored) {}
        return false;
    }

    /*
     * 根据用户昵称查找用户
     * */
    @Cacheable(key="'user-one-'+#p0")
    public User getByName(String name) {
        return userDao.findByUsername(name);
    }

    /*
     * 获取用户角色
     * */
    @Cacheable(key="'role-one-by-user-'+#p0")
    public int getRoleByUser(int uid) {
        try {
            int role = roleDao.getRoleByUser(uid);
            if (role > 0)
                return role;
        } catch (Exception e) {
            logger.error("用户id: " + uid + "无角色相关记录");
        }

        return 1;
    }

    /*
     * 设置用户角色
     * */
    public void setUserRole(User user) {
        if (user != null) {
            user.setRole(getRoleByUser(user.getId()));
        }
    }

    /*
     * 设置用户角色
     * */
    public void setUserRole(List<User> users) {
        if (!CollectionUtils.isEmpty(users)) {
            for (User user : users) {
                setUserRole(user);
            }
        }
    }

    /*
    * 添加 or 更新用户角色
    * */
    @CacheEvict(allEntries=true)
    public void relateUserRole(User bean) {
        User user = getByName(bean.getUsername());
        if (isRoleExist(user.getId()))
            roleDao.authorize(bean.getRole(), user.getId());
        else
            roleDao.addUserRole(user.getId(), bean.getRole());
        bean.setId(user.getId());
    }
}
