package com.lianyikai.campusblog.service;

import com.lianyikai.campusblog.dao.ReadRecordDao;
import com.lianyikai.campusblog.pojo.Article;
import com.lianyikai.campusblog.pojo.ReadRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReadRecordService {
    public static final ReadRecordService ME = new ReadRecordService();

    @Autowired
    ReadRecordDao readRecordDao;

    /*
    * 记录阅读行为
    * */
    public void read(ReadRecord bean) {
        readRecordDao.save(bean);
    }

    /*
    * 文章阅读数
    * */
    public int countByArticle(Article article) {
        return readRecordDao.countByArticle(article);
    }
}
