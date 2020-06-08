package com.lianyikai.campusblog.dao;

import com.lianyikai.campusblog.pojo.Article;
import com.lianyikai.campusblog.pojo.Comment;
import com.lianyikai.campusblog.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

// jpa持久层
public interface CommentDao extends JpaRepository<Comment,Integer> {
    // 统计未读评论数量
    int countByIsRead(int value);
    // 标记已读
    @Transactional
    @Modifying
    @Query(value = "UPDATE comment_record SET isRead=1;", nativeQuery = true)
    void updateIsRead();
    // 获取最近的count条评论
    @Query(value = "SELECT * FROM comment_record ORDER BY createdAt DESC limit ?1", nativeQuery = true)
    List<Comment> getRecentCount(int count);
    // 统计文章评论数
    int countByArticle(Article article);
    // 查询文章评论
    Page<Comment> findAllByArticle(Pageable var1, Article article);
    // 删除文章下的评论
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM comment_record WHERE articleId=?1", nativeQuery = true)
    void deleteByArticle(int id);
    // 查询用户评论
    Page<Comment> findAllByAnswerer(Pageable var1, User user);
    // 用户评论数
    int countByAnswerer(User user);
    // 查询用户消息
    Page<Comment> findAllByRespondent(Pageable var1, User user);
    // 用户消息数
    int countByRespondentAndIsRead(User user, int isRead);
    // 标记消息已读
    @Transactional
    @Modifying
    @Query(value = "UPDATE comment_record SET isRead=1 WHERE respondentId=?1", nativeQuery = true)
    void updateMessageIsRead(int uid);
}
