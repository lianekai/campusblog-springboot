package com.lianyikai.campusblog.service;

import com.lianyikai.campusblog.dao.MessageDao;
import com.lianyikai.campusblog.pojo.Message;
import com.lianyikai.campusblog.utils.Page4Navigator;
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
@CacheConfig(cacheNames="messages")
public class MessageService {
    @Autowired
    MessageDao messageDao;

    /*
     * 留言列表(按照创建时间倒排序)
     * */
    @Cacheable(key="'all-messages-order'")
    public List<Message> listOrderPubDesc() {
        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        return messageDao.findAll(sort);
    }

    /*
     * 获取最近count条留言
     * */
    @Cacheable(key="'recent-count-messages-'+#p0")
    public List<Message> listIndexMessages(int count) {
        return messageDao.getRecentCount(count);
    }

    /*
     * 留言数量
     * */
    @Cacheable(key="'all-messages-count'")
    public long countAll() {
        return messageDao.count();
    }

    /*
     * 留言列表分页
     * */
    @Cacheable(key="'messages-page-'+#p0+'-'+#p1")
    public Page4Navigator<Message> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<Message> pageFromJPA = messageDao.findAll(pageable);
        return new Page4Navigator<>(start + 1, pageFromJPA, navigatePages);
    }

    /*
     * 添加留言
     * */
    @CacheEvict(allEntries=true)
    public void add(Message bean) {
        messageDao.save(bean);
    }

    /*
     * 删除留言
     * */
    @CacheEvict(allEntries=true)
    public void delete(int id) {
        messageDao.deleteById(id);
    }
}
