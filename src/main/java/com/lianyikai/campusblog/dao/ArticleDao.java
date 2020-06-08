package com.lianyikai.campusblog.dao;

import com.lianyikai.campusblog.pojo.Article;
import com.lianyikai.campusblog.pojo.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

// jpa持久层
public interface ArticleDao extends JpaRepository<Article,Integer> {
    // 统计该分类下博客数量
    int countByCategory(Category category);
    // 统计标签文章数量
    @Query(value = "SELECT COUNT(*) FROM article WHERE FIND_IN_SET(?1, tags) > 0", nativeQuery = true)
    int countArticleOfTag(int id);
    // 上一篇
    @Query(value = "SELECT * FROM article WHERE id < ?1 ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Article getLast(int id);
    // 下一篇
    @Query(value = "SELECT * FROM article WHERE id > ?1 ORDER BY id ASC LIMIT 1", nativeQuery = true)
    Article getNext(int id);
    // 获取分类下的文章列表
    Page<Article> findByCategory(Pageable page, Category category);
    // 获取时间点列表
    @Query(value = "SELECT DISTINCT publishDate FROM article ORDER BY publishDate DESC", nativeQuery = true)
    List<Date> listDate();
    // 该月文章数量
    @Query(value = "SELECT COUNT(id) FROM article WHERE date_format(publishDate,'%Y-%m')=?1", nativeQuery = true)
    int countByMonth(String date);
    // 获取时间点下的文章列表
    @Query(value = "SELECT * FROM article WHERE date_format(publishDate,'%Y-%m')=?1", nativeQuery = true)
    Page<Article> findByDate(Pageable page, String date);
    // 标签下的文章列表
    @Query(value = "SELECT * FROM article WHERE FIND_IN_SET(?1, tags) > 0", nativeQuery = true)
    Page<Article> findByTag(Pageable page, int tid);
    // 评论最多的文章
    @Query(value = "SELECT articleId, COUNT(id) AS count FROM comment_record GROUP BY articleId ORDER BY count DESC limit 5", nativeQuery = true)
    List<Integer> listMostComment();
    // 访问最多的文章
    @Query(value = "SELECT aid, COUNT(id) AS count FROM read_record GROUP BY aid ORDER BY count DESC LIMIT 5", nativeQuery = true)
    List<Integer> listMostVisit();
    // 根据id加载文章列表
    @Query(value = "SELECT * FROM article WHERE id IN (?1)", nativeQuery = true)
    List<Article> loadlist(List<Integer> ids);
}
