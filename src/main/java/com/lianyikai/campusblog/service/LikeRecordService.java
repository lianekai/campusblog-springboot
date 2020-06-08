package com.lianyikai.campusblog.service;

import com.lianyikai.campusblog.dao.ArticleDao;
import com.lianyikai.campusblog.dao.LikeRecordDao;
import com.lianyikai.campusblog.pojo.Article;
import com.lianyikai.campusblog.pojo.LikeRecord;
import com.lianyikai.campusblog.pojo.User;
import com.lianyikai.campusblog.utils.Page4Navigator;
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

@Service
@CacheConfig(cacheNames="article_like_records")
public class LikeRecordService {
    public static final LikeRecordService ME = new LikeRecordService();
    private static final int NOT_READ = 0;

    @Autowired
    LikeRecordDao likeRecordDao;
    @Autowired
    ArticleDao articleDao;

    /*
    * 统计未读消息数量
    * */
    @Cacheable(key="'no-read-count'")
    public long countNotRead() {
        return likeRecordDao.countByIsRead(NOT_READ);
    }

    /*
    * 点赞消息列表分页
    * */
    @Cacheable(key="'records-page-'+#p0+'-'+#p1")
    public Page4Navigator<LikeRecord> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "likeDate");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<LikeRecord> pageFromJPA = likeRecordDao.findAll(pageable);
        return new Page4Navigator<>(start + 1, pageFromJPA, navigatePages);
    }

    /*
     * 点赞
     * */
    @Caching(evict = {@CacheEvict(value = "article_like_records", allEntries = true)
//            @CacheEvict(value = "articles", key = "'articles-page-0-10'"),
            })
    public void add(LikeRecord bean) {
        likeRecordDao.save(bean);
    }

    /*
     * 删除点赞记录
     * */
    @CacheEvict(value = {"articles", "article_like_records"}, allEntries=true)
    public void delete(int id) {
        likeRecordDao.deleteById(id);
    }

    /*
     * 全部标记已读
     * */
    @CacheEvict(allEntries=true)
    public void allOfRead() {
        likeRecordDao.updateIsRead();
    }

    /*
     * 统计文章点赞数
     * */
    @Cacheable(key="'reads-count-by-article-'+#p0")
    public int countByArticle(int id) {
        Article article = articleDao.getOne(id);
        if (article == null)
            return 0;
        return likeRecordDao.countByArticle(article);
    }

    /*
     * 用户是否已经点赞过这篇文章
     * */
    @Cacheable(key="'is-liked-'+#p0+'-'+#p1")
    public boolean isLiked(int uid, int aid) {
        try {
            LikeRecord record = likeRecordDao.isLiked(uid, aid);
            if (record != null && record.getId()>0)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
     * 用户点赞数
     * */
    @Cacheable(key="'count-liked-'+#p0.id")
    public int countByUser(User user) {
        return likeRecordDao.countByUser(user);
    }

    /*
     * 用户点赞列表
     * */
    @Cacheable(key="'records-page-'+#p0+'-'+#p1+'-'+#p2.id")
    public Page4Navigator<LikeRecord> listByUser(int start, int size, User user, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "likeDate");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<LikeRecord> pageFromJPA = likeRecordDao.findByUser(pageable, user);
        return new Page4Navigator<>(start + 1, pageFromJPA, navigatePages);
    }
}
