package com.lianyikai.campusblog.dao;

import com.lianyikai.campusblog.pojo.SiteInfo;
import org.springframework.data.jpa.repository.JpaRepository;

// jpa持久层
public interface SiteInfoDao extends JpaRepository<SiteInfo,Integer> {

}
