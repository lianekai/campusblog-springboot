package com.lianyikai.campusblog.dao;

import com.lianyikai.campusblog.pojo.Article;
import com.lianyikai.campusblog.pojo.ReadRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

// jpa持久层
public interface ReadRecordDao extends JpaRepository<ReadRecord,Integer> {
    // 文章阅读数
    int countByArticle(Article article);
    // 删除文章阅读记录
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM read_record WHERE aid=?1", nativeQuery = true)
    void deleteByArticle(int id);
}
