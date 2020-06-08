package com.lianyikai.campusblog.dao;

import com.lianyikai.campusblog.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

// jpa持久层
public interface UserDao extends JpaRepository<User,Integer> {
    User findByUsername(String name);
    User getByUsernameAndPassword(String username, String password);
}
