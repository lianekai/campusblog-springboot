package com.lianyikai.campusblog.dao;

import com.lianyikai.campusblog.pojo.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;

// jpa持久层
public interface FeedBackDao extends JpaRepository<FeedBack,Integer> {

}
