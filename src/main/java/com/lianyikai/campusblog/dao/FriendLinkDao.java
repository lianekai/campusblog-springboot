package com.lianyikai.campusblog.dao;

import com.lianyikai.campusblog.pojo.FriendLink;
import org.springframework.data.jpa.repository.JpaRepository;

// jpa持久层
public interface FriendLinkDao extends JpaRepository<FriendLink,Integer> {

}
