package com.lianyikai.campusblog.dao;

import com.lianyikai.campusblog.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

// jpa持久层
public interface CategoryDao extends JpaRepository<Category,Integer> {
    // 根据名称查找分类
    Category findByName(String name);
}
