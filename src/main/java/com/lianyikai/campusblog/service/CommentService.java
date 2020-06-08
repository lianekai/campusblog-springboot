package com.lianyikai.campusblog.service;

import com.lianyikai.campusblog.dao.ArticleDao;
import com.lianyikai.campusblog.dao.CommentDao;
import com.lianyikai.campusblog.dao.CommentLikeDao;
import com.lianyikai.campusblog.utils.Page4Navigator;
import com.lianyikai.campusblog.pojo.Article;
import com.lianyikai.campusblog.pojo.Comment;
import com.lianyikai.campusblog.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@CacheConfig(cacheNames="comments")
public class CommentService {
    private static final int NOT_READ = 0;

    @Autowired
    CommentDao commentDao;
    @Autowired
    ArticleDao articleDao;
    @Autowired
    CommentLikeDao commentLikeDao;

    /*
     * 评论列表(按照创建时间倒排序)
     * */
    @Cacheable(key="'all-comments-order'")
    public List<Comment> listOrderPubDesc() {
        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        return commentDao.findAll(sort);
    }

    /*
     * 获取最近count条评论
     * */
    @Cacheable(key="'recent-comments-count-'+#p0")
    public List<Comment> listIndexComments(int count) {
        return commentDao.getRecentCount(count);
    }

    /*
     * 评论数量
     * */
    @Cacheable(key="'all-comments-count'")
    public long countAll() {
        return commentDao.count();
    }

    /*
     * 未读评论数量
     * */
    @Cacheable(key="'no-read-comments-count'")
    public long countNoRead() {
        return commentDao.countByIsRead(NOT_READ);
    }

    /*
     * 评论列表分页
     * */
    @Cacheable(key="'comments-page-'+#p0+'-'+#p1")
    public Page4Navigator<Comment> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<Comment> pageFromJPA = commentDao.findAll(pageable);
        return new Page4Navigator<>(start + 1, pageFromJPA, navigatePages);
    }

    /*
     * 用户评论列表分页
     * */
    @Cacheable(key="'comments-page-'+#p0+'-'+#p1+'-'+#p2.id")
    public Page4Navigator<Comment> listOfUser(int start, int size, User user, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<Comment> pageFromJPA = commentDao.findAllByAnswerer(pageable, user);
        return new Page4Navigator<>(start + 1, pageFromJPA, navigatePages);
    }

    /*
     * 用户评论列表分页
     * */
    @Cacheable(key="'messages-page-'+#p0+'-'+#p1+'-'+#p2.id")
    public Page4Navigator<Comment> listMessageOfUser(int start, int size, User user, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<Comment> pageFromJPA = commentDao.findAllByRespondent(pageable, user);
        return new Page4Navigator<>(start + 1, pageFromJPA, navigatePages);
    }

    /*
     * 添加评论
     * */
    @Caching(evict = {@CacheEvict(value = "comments", allEntries = true),
            @CacheEvict(value = "articles", key = "'articles-by-most-comment'")
    })
    public void add(Comment bean) {
        commentDao.save(bean);
    }

    /*
     * 删除评论
     * */
    @CacheEvict(allEntries=true)
    public void delete(int id) {
        commentDao.deleteById(id);
    }

    /*
     * 全部标记已读
     * */
    @CacheEvict(allEntries=true)
    public void allOfRead() {
        commentDao.updateIsRead();
    }

    /*
     * 全部消息标记已读
     * */
    @CacheEvict(allEntries=true)
    public void allMessageRead(int uid) {
        commentDao.updateMessageIsRead(uid);
    }

    /*
     * 统计文章评论数
     * */
    @Cacheable(key="'comments-count-by-article-'+#p0")
    public int countByArticle(int id) {
        Article article = articleDao.getOne(id);
        if (article == null)
            return 0;
        return commentDao.countByArticle(article);
    }

    /*
     * 文章详情评论列表分页
     * */
    @Cacheable(key="'comments-by-article-'+#p0+'-'+#p1+'-'+#p2")
    public Page4Navigator<Comment> list4Article(int aid, int start, int size, int navigatePages) {
        Article article = articleDao.getOne(aid);
        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<Comment> pageFromJPA = commentDao.findAllByArticle(pageable, article);
        return new Page4Navigator<>(start + 1, pageFromJPA, navigatePages);
    }

    /*
     * 用户评论数
     * */
    @Cacheable(key="'comments-count-by-user-'+#p0.id")
    public int countByUser(User user) {
        return commentDao.countByAnswerer(user);
    }

    /*
     * 用户未读消息数
     * */
    @Cacheable(key="'messages-count-by-user-'+#p0.id")
    public int countMessageByUser(User user) {
        return commentDao.countByRespondentAndIsRead(user, Comment.IS_NOT_READ);
    }

    /*
     * 设置父评论
     * */
    public void setCommentParent(Comment comment) {
        if (comment != null && comment.getpId() > 0) {
            comment.setParent(commentDao.getOne(comment.getpId()));
        }
    }

    /*
     * 设置父评论
     * */
    public void setCommentParent(List<Comment> comments) {
        if (!CollectionUtils.isEmpty(comments)) {
            for (Comment comment : comments)
                setCommentParent(comment);
        }
    }

    /*
     * 设置评论点赞数
     * */
    private void setLikeCount(Comment comment) {
        if (comment != null) {
            comment.setLikeCount(commentLikeDao.countByComment(comment));
        }
    }

    /*
     * 设置评论点赞数
     * */
    public void setLikeCount(List<Comment> comments) {
        if (!CollectionUtils.isEmpty(comments)) {
            for (Comment comment : comments)
                setLikeCount(comment);
        }
    }
}
