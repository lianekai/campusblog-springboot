package com.lianyikai.campusblog.service;

import com.lianyikai.campusblog.dao.FeedBackDao;
import com.lianyikai.campusblog.utils.Page4Navigator;
import com.lianyikai.campusblog.pojo.FeedBack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames="feed_backs")
public class FeedBackService {
    @Autowired
    FeedBackDao feedBackDao;

    /*
     * 反馈列表(按照创建时间倒排序)
     * */
    @Cacheable(key="'all-feedbacks-order'")
    public List<FeedBack> listPubDesc() {
        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        return feedBackDao.findAll(sort);
    }

    /*
     * 反馈数量
     * */
    @Cacheable(key="'all-feedbacks-count'")
    public long countAll() {
        return feedBackDao.count();
    }

    /*
     * 反馈列表分页
     * */
    @Cacheable(key="'feedbacks-page-'+#p0+'-'+#p1")
    public Page4Navigator<FeedBack> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<FeedBack> pageFromJPA = feedBackDao.findAll(pageable);
        return new Page4Navigator<>(start + 1, pageFromJPA, navigatePages);
    }

    /*
     * 添加反馈
     * */
    @CacheEvict(allEntries=true)
    public void add(FeedBack bean) {
        feedBackDao.save(bean);
    }

    /*
     * 删除反馈
     * */
    @CacheEvict(allEntries=true)
    public void delete(int id) {
        feedBackDao.deleteById(id);
    }
}
