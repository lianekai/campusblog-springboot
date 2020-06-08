package com.lianyikai.campusblog.dao;

import com.lianyikai.campusblog.pojo.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// jpa持久层
public interface MessageDao extends JpaRepository<Message,Integer> {
    // 获取最近的count条评论
    @Query(value = "SELECT * FROM leave_message_record ORDER BY createdAt DESC limit ?1", nativeQuery = true)
    List<Message> getRecentCount(int count);
}
