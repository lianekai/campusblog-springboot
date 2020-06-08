package com.lianyikai.campusblog.service;

import com.lianyikai.campusblog.dao.VisitRecordDao;
import com.lianyikai.campusblog.pojo.VisitRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames="visit_records")
public class VisitRecordService {
    @Autowired
    VisitRecordDao visitRecordDao;

    /*
    * 统计访问总量
    * */
    @Cacheable(key="'all-records-count'")
    public long countAll() {
        return visitRecordDao.count();
    }

    /*
    * 统计昨天访问量
    * */
    @Cacheable(key="'all-records-yesterdat-count'")
    public long countYesterday() {
        return visitRecordDao.countYesterdayVisit();
    }

    /*
     * 访问
     * */
    @CacheEvict(allEntries=true)
    public void add(VisitRecord bean) {
        visitRecordDao.save(bean);
    }
}
