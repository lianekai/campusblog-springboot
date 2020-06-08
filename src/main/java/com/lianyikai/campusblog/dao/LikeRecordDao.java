package com.lianyikai.campusblog.dao;

import com.lianyikai.campusblog.pojo.Article;
import com.lianyikai.campusblog.pojo.LikeRecord;
import com.lianyikai.campusblog.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

// jpa持久层
public interface LikeRecordDao extends JpaRepository<LikeRecord,Integer> {
    // 统计博客喜欢数量
    int countByArticle(Article article);
    // 统计未读消息数量
    int countByIsRead(int value);
    // 标记已读
    @Transactional
    @Modifying
    @Query(value = "UPDATE article_likes_record SET isRead=1;", nativeQuery = true)
    void updateIsRead();
    // 用户是否已经点赞
    @Query(value = "SELECT * FROM article_likes_record WHERE userId=?1 AND articleId=?2 limit 1", nativeQuery = true)
    LikeRecord isLiked(int uid, int aid);
    // 标记已读
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM article_likes_record WHERE articleId=?1", nativeQuery = true)
    void deleteByArticle(int id);
    // 用户点赞数
    int countByUser(User user);
    // 用户点赞列表
    Page<LikeRecord> findByUser(Pageable var1, User user);
}
