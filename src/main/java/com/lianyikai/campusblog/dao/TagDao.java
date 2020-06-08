package com.lianyikai.campusblog.dao;

import com.lianyikai.campusblog.pojo.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

// jpa持久层
public interface TagDao extends JpaRepository<Tag,Integer> {
    // 根据名称查找分类
    Tag findByName(String name);
}
