package com.lianyikai.campusblog.dao;

import com.lianyikai.campusblog.pojo.VisitRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// jpa持久层
public interface VisitRecordDao extends JpaRepository<VisitRecord,Integer> {
    // 统计昨日访问量
    @Query(value = "SELECT COUNT(id) FROM visit_record WHERE (TO_DAYS(NOW())-TO_DAYS(time))=1", nativeQuery = true)
    long countYesterdayVisit();
}
