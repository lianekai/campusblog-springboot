package com.lianyikai.campusblog.service;

import com.lianyikai.campusblog.dao.FriendLinkDao;
import com.lianyikai.campusblog.utils.Page4Navigator;
import com.lianyikai.campusblog.pojo.FriendLink;
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
@CacheConfig(cacheNames="links")
public class FriendLinkService {
    @Autowired
    FriendLinkDao friendLinkDao;

    /*
     * 友链列表(按照创建时间倒排序)
     * */
    @Cacheable(key="'all-links-order'")
    public List<FriendLink> listPubDesc() {
        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        return friendLinkDao.findAll(sort);
    }

    /*
     * 友链数量
     * */
    @Cacheable(key="'all-links-count'")
    public long countAll() {
        return friendLinkDao.count();
    }

    /*
     * 友链接列表分页
     * */
    @Cacheable(key="'links-page-'+#p0+'-'+#p1")
    public Page4Navigator<FriendLink> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<FriendLink> pageFromJPA = friendLinkDao.findAll(pageable);
        return new Page4Navigator<>(start + 1, pageFromJPA, navigatePages);
    }

    /*
     * 添加友链
     * */
    @CacheEvict(allEntries=true)
    public void add(FriendLink bean) {
        friendLinkDao.save(bean);
    }

    /*
     * 删除友链接
     * */
    @CacheEvict(allEntries=true)
    public void delete(int id) {
        friendLinkDao.deleteById(id);
    }

    /*
     * 通过id获取友链
     * */
    @Cacheable(key="'link-one-'+#p0")
    public FriendLink getById(int id) {
        return friendLinkDao.getOne(id);
    }

    /*
     * 编辑友链接
     * */
    @CacheEvict(allEntries=true)
    public void update(FriendLink bean) {
        friendLinkDao.save(bean);
    }
}
