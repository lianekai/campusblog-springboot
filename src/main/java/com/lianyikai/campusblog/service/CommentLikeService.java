package com.lianyikai.campusblog.service;

import com.lianyikai.campusblog.dao.CommentLikeDao;
import com.lianyikai.campusblog.pojo.CommentLikeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames="comments_like")
public class CommentLikeService {
    @Autowired
    CommentLikeDao commentLikeDao;

    /*
     * 点赞评论
     * */
    @CacheEvict(allEntries=true)
    public void add(CommentLikeRecord bean) {
        commentLikeDao.save(bean);
    }

    /*
     * 取消点赞评论
     * */
    @CacheEvict(allEntries=true)
    public void delete(int uid, int cid) {
        commentLikeDao.cancel(uid, cid);
    }

    /*
     * 判断该用户是否已点赞该条评论
     * */
    @Cacheable(key="'is-liked-'+#p0+'-'+#p1")
    public boolean isLiked(int uid, int cid) {
        CommentLikeRecord record = commentLikeDao.isLiked(uid, cid);
        return record != null && record.getId() > 0;
    }
}
