package com.lianyikai.campusblog.dao;

import com.lianyikai.campusblog.pojo.Comment;
import com.lianyikai.campusblog.pojo.CommentLikeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

// jpa持久层
public interface CommentLikeDao extends JpaRepository<CommentLikeRecord,Integer> {
    // 统计评论点赞数量
    int countByComment(Comment comment);
    // 用户是否已经点赞
    @Query(value = "SELECT * FROM comment_likes_record WHERE uid=?1 AND cid=?2 limit 1", nativeQuery = true)
    CommentLikeRecord isLiked(int uid, int cid);
    // 取消点赞
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM comment_likes_record WHERE uid=?1 AND cid=?2", nativeQuery = true)
    void cancel(int uid, int cid);
}
