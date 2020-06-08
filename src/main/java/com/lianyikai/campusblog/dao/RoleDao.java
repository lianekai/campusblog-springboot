package com.lianyikai.campusblog.dao;

import com.lianyikai.campusblog.pojo.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

// jpa持久层
public interface RoleDao extends JpaRepository<Role,Integer> {
    // 获取用户角色
    @Query(value = "SELECT IFNULL(role_id,1) FROM user_role WHERE user_id=?1 limit 1", nativeQuery = true)
    int getRoleByUser(int uid);
    // 记录是否存在
    @Query(value = "SELECT role_id FROM user_role WHERE user_id=?1 limit 1", nativeQuery = true)
    int isExist(int uid);
    // 添加用户－角色关联
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO user_role(user_id,role_id) VALUES (?1,?2)",nativeQuery = true)
    int addUserRole(int uid,int rid);
    // 授权
    @Transactional
    @Modifying
    @Query(value = "UPDATE user_role SET role_id=?1 WHERE user_id=?2", nativeQuery = true)
    void authorize(int rid, int uid);
}
