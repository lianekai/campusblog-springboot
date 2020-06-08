package com.lianyikai.campusblog.service;

import com.lianyikai.campusblog.dao.ArticleDao;
import com.lianyikai.campusblog.dao.CategoryDao;
import com.lianyikai.campusblog.utils.Page4Navigator;
import com.lianyikai.campusblog.pojo.Category;
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

import java.util.List;

@Service
@CacheConfig(cacheNames="categories")
public class CategoryService {
    public static final CategoryService ME = new CategoryService();

    @Autowired
    CategoryDao categoryDao;
    @Autowired
    ArticleDao articleDao;

    /*
    * 根据发布时间倒排序列举博客
    * */
    @Cacheable(key="'all-categories-order'")
    public List<Category> listOrderPubDesc() {
        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        List<Category> categories = categoryDao.findAll(sort);
        countArticleByCategory(categories);
        return categories;
    }

    /*
     * 分类数量
     * */
    @Cacheable(key="'all-categories-count'")
    public long countAll() {
        return categoryDao.count();
    }

    /*
     * 点赞消息列表分页
     * */
    @Cacheable(key="'categories-page-'+#p0+'-'+#p1")
    public Page4Navigator<Category> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<Category> pageFromJPA = categoryDao.findAll(pageable);
        countArticleByCategory(pageFromJPA.getContent());
        return new Page4Navigator<>(start + 1, pageFromJPA, navigatePages);
    }

    /*
     * 添加分类
     * */
    @CacheEvict(allEntries=true)
    public void add(Category bean) {
        categoryDao.save(bean);
    }

    /*
     * 删除分类
     * */
    @CacheEvict(allEntries=true)
    public void delete(int id) {
        categoryDao.deleteById(id);
    }

    /*
     * 统计该分类下博客数量
     * */
    @Cacheable(key="'articles-count-by-category'")
    public void countArticleByCategory(List<Category> categories) {
        if (!CollectionUtils.isEmpty(categories)) {
            for (Category category : categories) {
                category.setArticleCount(articleDao.countByCategory(category));
            }
        }
    }

    /*
     * 判断该分类是否存在
     * */
    @Cacheable(key="'is-exist-'+#p0")
    public boolean isExist(String name) {
        return categoryDao.findByName(name) != null;
    }

    /*
    * 根据id获取category对象
    * */
    @Cacheable(key="'category-one-'+#p0")
    public Category getById(int id) {
        return categoryDao.getOne(id);
    }
}
