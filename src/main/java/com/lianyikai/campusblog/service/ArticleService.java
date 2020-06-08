package com.lianyikai.campusblog.service;


import com.lianyikai.campusblog.dao.*;
import com.lianyikai.campusblog.utils.Page4Navigator;
import com.lianyikai.campusblog.pojo.Article;
import com.lianyikai.campusblog.pojo.Category;
import com.lianyikai.campusblog.pojo.Tag;
import com.lianyikai.campusblog.utils.ESUtil;
import com.lianyikai.campusblog.utils.HTMLUtils;
import org.elasticsearch.search.SearchHits;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@CacheConfig(cacheNames="articles")
public class ArticleService {
    public static final ArticleService ME = new ArticleService();
    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    ArticleDao articleDao;
    @Autowired
    TagDao tagDao;
    @Autowired
    LikeRecordDao likeRecordDao;
    @Autowired
    CommentDao commentDao;
    @Autowired
    CommentLikeDao commentLikeDao;
    @Autowired
    ReadRecordDao readRecordDao;

    /*
    * 根据发布时间倒排序列举博客
    * */
    @Cacheable(key="'all-articles-order'")
    public List<Article> listOrderPubDesc() {
        Sort sort = new Sort(Sort.Direction.DESC, "publishDate");
        List<Article> articles = articleDao.findAll(sort);
        setArticleTagsAndLikesAndImg(articles);
        return articles;
    }

    /*
    * 统计所有博客数量
    * */
    @Cacheable(key="'all-articles-count'")
    public long countAll() {
        return articleDao.count();
    }

    /*
    * 博客列表分页
    * */
    @Cacheable(key="'articles-page-'+#p0+'-'+#p1")
    public Page4Navigator<Article> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "publishDate");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<Article> pageFromJPA = articleDao.findAll(pageable);
        setArticleTagsAndLikesAndImg(pageFromJPA.getContent());
        return new Page4Navigator<>(start + 1, pageFromJPA, navigatePages);
    }

    /*
     * 博客列表分页
     * */
    @Cacheable(key="'articles-page-by-category'+#p0.id+'-'+#p1+'-'+#p2")
    public Page4Navigator<Article> listOfCategory(Category bean, int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "publishDate");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<Article> pageFromJPA = articleDao.findByCategory(pageable, bean);
        setArticleTagsAndLikesAndImg(pageFromJPA.getContent());
        return new Page4Navigator<>(start + 1, pageFromJPA, navigatePages);
    }

    /*
     * 博客列表分页
     * */
    @Cacheable(key="'articles-page-by-date'+#p0+'-'+#p1+'-'+#p2")
    public Page4Navigator<Article> listOfDate(String date, int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "publishDate");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<Article> pageFromJPA = articleDao.findByDate(pageable, date);
        setArticleTagsAndLikesAndImg(pageFromJPA.getContent());
        return new Page4Navigator<>(start + 1, pageFromJPA, navigatePages);
    }

    /*
     * 博客列表分页
     * */
    @Cacheable(key="'articles-page-by-tag'+#p0+'-'+#p1+'-'+#p2")
    public Page4Navigator<Article> listOfTag(int tid, int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "publishDate");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<Article> pageFromJPA = articleDao.findByTag(pageable, tid);
        if (pageFromJPA.getContent().size() > 0) {
            setArticleTagsAndLikesAndImg(pageFromJPA.getContent());
            return new Page4Navigator<>(start + 1, pageFromJPA, navigatePages);
        }
        return null;
    }

    /*
     * 设置博客标签&喜欢数
     * */
    public void setArticleTagsAndLikesAndImg(List<Article> articles) {
        if (CollectionUtils.isEmpty(articles))
            return ;
        for (Article article : articles) {
            if (article != null) {
                setArticleTagsAndLikes(article);
            }
        }
    }

    /*
     * 设置博客标签&喜欢数
     * */
    public void setArticleTagsAndLikes(Article article) {
        if (!StringUtils.isEmpty(article.getTags())) {
            String[] ids = article.getTags().split(",");
            List<Integer> list = new ArrayList<>();
            for (String id : ids) {
                list.add(Integer.valueOf(id));
            }
            List<Tag> tags = tagDao.findAllById(list);
            article.setTagList(tags);
            article.setTagsId(list);
        }
        article.setLikes(likeRecordDao.countByArticle(article));
        article.setImg(getFirstImgSrc(article.getContent()));
    }

    /*
     * 添加博客
     * */
    @CacheEvict(value = {"articles", "categories", "tags"}, allEntries=true)
    public void add(Article bean) throws IOException {
        articleDao.save(bean);
        ESUtil.addESDocument(bean);
    }

    /*
     * 根据ID获取博客
     * */
    @Cacheable(key="'article-one-'+#p0")
    public Article getById(int id) {
        try {
            if (id > 0) {
                Article article = articleDao.getOne(id);
                article.setLast(articleDao.getLast(id));
                article.setNext(articleDao.getNext(id));
                return article;
            }
        } catch (Exception e) {
            log.info("文章不存在 id: "+id);
        }
        return null;
    }

    /*
     * 编辑博客
     * */
    @CacheEvict(value = {"articles", "categories", "tags"}, allEntries=true)
    public void update(Article bean) throws IOException {
        articleDao.save(bean);
        ESUtil.updateESDocument(bean);
    }

    /*
     * 设置博客标签
     * */
    public void setTags(Article bean) {
        List<Integer> tags = bean.getTagsId();
        if (!CollectionUtils.isEmpty(tags)) {
            StringBuilder sb = new StringBuilder();
            for (Integer tag : tags) {
                sb.append(tag + ",");
            }
            bean.setTags(sb.toString());
        }
    }

    /*
     * 设置博客简述
     * */
    public void setDesc(Article bean) {
        if (!StringUtils.isEmpty(bean.getContent())) {
            String desc = HTMLUtils.summary(bean.getContent(), 150);
            bean.setDescription(desc + "...");
        }
    }

    /*
     * 设置博客正文内容图片可点击展示原图
     * */
    public void setContentSourceImg(Article bean) {
        //使用Jsoup转化为doc
        Document doc = Jsoup.parse(bean.getContent());
        //根据标签名来查找节点，此处要查找的是<img>标签
        Elements imgs =  doc.getElementsByTag("img");
        if (!imgs.isEmpty()) {
            for (Element img : imgs) {
                String src = img.attr("src");
                img.before("<a class=\"example-image-link\" href=" + src
                        + " data-lightbox=\"example-set\" data-title=\"Picture\">" + img + "</a>");
                img.remove();
            }
            bean.setContent(doc.html());
        }
    }

    /*
     * 删除博客
     * */
    @CacheEvict(value = {"articles", "categories", "comments", "article_like_records", "tags"}, allEntries=true)
    public void delete(int id) throws IOException {
        commentDao.deleteByArticle(id);
        likeRecordDao.deleteByArticle(id);
        readRecordDao.deleteByArticle(id);
        articleDao.deleteById(id);
        ESUtil.delESDocument(id);
    }

    /*
     * 时间点列表
     * */
    @Cacheable(key="'articles-date-all'")
    public List<String> listDate() {
        List<Date> dates = articleDao.listDate();
        List<String> dateList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dates))
            for (Date date : dates) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
                String str = formatter.format(date);
                if (!dateList.contains(str)) {
                    dateList.add(str);
                }
            }

        return dateList;
    }

    /*
     * 时间段文章数量
     * */
    @Cacheable(key="'articles-count-by-date-'+#p0")
    public int countByMonth(String date) {
        return articleDao.countByMonth(date);
    }

    /*
     * 评论最多的文章
     * */
    @Cacheable(key="'articles-by-most-comment'")
    public List<Article> listMostComment() {
        return loadlistOrder(articleDao.listMostComment());
    }

    /*
     * 访问最多的文章
     * */
//    @Cacheable(key="'articles-by-most-visit'")
    public List<Article> listMostVisit() {
        return loadlistOrder(articleDao.listMostVisit());
    }

    /*
     * 根据id加载文章列表(无序)
     * */
    private List<Article> loadlist(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids))
            return null;
        return articleDao.loadlist(ids);
    }

    /*
     * 根据id加载文章列表(有序)
     * */
    private List<Article> loadlistOrder(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids))
            return null;
        List<Article> articles = new ArrayList<>();
        for (int id : ids) {
            Article article = articleDao.getOne(id);
            if (article != null)
                articles.add(article);
        }
        return articles;
    }

    /*
     * 抓取文章第一张图片
     * */
    public String getFirstImgSrc(String htmlStr) {
        List<String> imgs = HTMLUtils.getImgSrc(htmlStr);
        if (CollectionUtils.isEmpty(imgs))
            return null;
        return imgs.get(0);
    }

    /*
    * 初始化数据库数据到es
    * */
    public void initDatabase2ES() throws IOException {
        List<Article> articles = articleDao.findAll();
        if (!CollectionUtils.isEmpty(articles)) {
            for (Article article : articles) {
                ESUtil.addESDocument(article);
            }
        }
    }

    /*
     * 搜索文章
     * */
    public SearchHits search(String keyword, int start, int size, int sort) throws IOException {
        return ESUtil.search(keyword, start, size, sort);
    }
}
