package com.lianyikai.campusblog.service;

import com.lianyikai.campusblog.dao.ArticleDao;
import com.lianyikai.campusblog.dao.TagDao;
import com.lianyikai.campusblog.pojo.Tag;
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
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@CacheConfig(cacheNames="tags")
public class TagService {
    @Autowired
    TagDao tagDao;
    @Autowired
    ArticleDao articleDao;

    /*
     * 标签列表(按照创建时间倒排序)
     * */
    @Cacheable(key="'all-tags-order'")
    public List<Tag> listPubDesc() {
        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        List<Tag> tags = tagDao.findAll(sort);
        setArticleCount(tags);
        return tags;
    }

    /*
     * 标签数量
     * */
    @Cacheable(key="'all-tags-count'")
    public long countAll() {
        return tagDao.count();
    }

    /*
     * 标签列表分页
     * */
    @Cacheable(key="'tags-page-'+#p0+'-'+#p1")
    public Page4Navigator<Tag> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<Tag> pageFromJPA = tagDao.findAll(pageable);
        Page4Navigator page = new Page4Navigator<>(start + 1, pageFromJPA, navigatePages);
        setArticleCount(page.getContent());
        return page;
    }

    /*
     * 添加标签
     * */
    @CacheEvict(allEntries=true)
    public void add(Tag bean) {
        tagDao.save(bean);
    }

    /*
     * 删除标签
     * */
    @CacheEvict(allEntries=true)
    public void delete(int id) {
        tagDao.deleteById(id);
    }

    /*
     * 通过id查找标签
     * */
    @Cacheable(key="'tag-one-'+#p0")
    public Tag getById(int id) {
        if (id > 0) {
            try {
                return tagDao.getOne(id);
            } catch (EntityNotFoundException ignored) {}
        }
        return null;
    }

    /*
     * 判断该分类是否存在
     * */
    @Cacheable(key="'is-exist-'+#p0")
    public boolean isExist(String name) {
        return tagDao.findByName(name) != null;
    }

    /*
     * 统计标签文章数量
     * */
    @Cacheable(key="'tag-one-'+#p0")
    public int countArticle(int id) {
        return articleDao.countArticleOfTag(id);
    }

    /*
     * 设置标签文章数量
     * */
    public void setArticleCount(Tag tag) {
        if (tag != null)
            tag.setArticleCount(countArticle(tag.getId()));
    }

    /*
     * 设置标签文章数量
     * */
    public void setArticleCount(List<Tag> tags) {
        if (!CollectionUtils.isEmpty(tags))
            for (Tag tag : tags)
                setArticleCount(tag);
    }
}
