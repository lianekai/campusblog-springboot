package com.lianyikai.campusblog.controller;

import com.alibaba.fastjson.JSONObject;

import com.lianyikai.campusblog.service.*;
import com.lianyikai.campusblog.comparator.ViewCountComparator;
import com.lianyikai.campusblog.pojo.Article;
import com.lianyikai.campusblog.pojo.Category;
import com.lianyikai.campusblog.pojo.User;

import com.lianyikai.campusblog.utils.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/*
* 博客控制器
* */
@RestController
public class ArticleController {
    @Autowired
    ArticleService articleService;
    @Autowired
    TagService tagService;
    @Autowired
    UserService userService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ReadRecordService readRecordService;
    @Autowired
    LikeRecordService likeRecordService;

    /*
     * 统计博客文章数量
     * */
    @GetMapping(value="/count_article_all")
    public Object countUserAll() {
        Map<String, Object> map = new HashMap<>();
        map.put("countArticleAll", articleService.countAll());
        return ResultApi.success(map);
    }

    /*
     * 博客列表(分页)
     * */
    @GetMapping(value="/articles")
    public Page4Navigator<Article> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "15") int size) {
        start = start < 0 ? 0 : start;
        // 分页
        Page4Navigator<Article> page = articleService.list(start, size, 100);
        articleService.setArticleTagsAndLikesAndImg(page.getContent());
        return page;
    }

    /*
     * 发布博客
     * */
    @PostMapping(value="/articles")
    public Object add(@RequestBody Article bean) throws IOException {
        // 转换并设置tags字段
        ArticleService.ME.setTags(bean);
        // 处理设置博客简述
        ArticleService.ME.setDesc(bean);
        // 处理文章内容图片
        if (!StringUtils.isEmpty(articleService.getFirstImgSrc(bean.getContent()))) {
            articleService.setContentSourceImg(bean);
        }
        Subject subject = SecurityUtils.getSubject();
        User user = new User();
        if (subject.isAuthenticated() || subject.isRemembered()) {
            user = userService.getByName(subject.getPrincipal().toString());
        } else {
            user.setId(1);
        }
        bean.setAuthor(user);
        bean.setPublishDate(new Date());
        bean.setUpdateDate(new Date());
        articleService.add(bean);
        return bean;
    }

    /*
     * 获取博客信息
     * */
    @GetMapping("/articles/{id}")
    public Article get(@PathVariable("id") int id) {
        Article article = articleService.getById(id);
        if (article == null) {
            return null;
        }
        articleService.setArticleTagsAndLikes(article);
        return article;
    }

    /*
     * 获取websocket链接
     * */
    @GetMapping("/articles/get_ws_link")
    public Object getWSLink() {
        Map<String, Object> map = new HashMap<>();
        map.put("wsLink", LinkTool.getHostOf("ws"));
        return ResultApi.success(map);
    }

    /*
     * 编辑博客
     * */
    @PutMapping("/articles")
    public Object update(@RequestBody Article bean) throws IOException {
        // 转换并设置tags字段
        ArticleService.ME.setTags(bean);
        // 处理设置博客简述
        ArticleService.ME.setDesc(bean);
        // 处理文章内容图片
        if (!StringUtils.isEmpty(articleService.getFirstImgSrc(bean.getContent()))) {
            articleService.setContentSourceImg(bean);
        }
        bean.setUpdateDate(new Date());
        articleService.update(bean);
        return bean;
    }

    /*
     * 删除博客
     * */
    @DeleteMapping("/articles/{id}")
    public Object delete(@PathVariable("id") int id, HttpServletRequest request) throws IOException {
        articleService.delete(id);
        return ResultApi.success();
    }

    /*
     * 上传图片
     * 返回格式
     * {
        uploaded: true,
        url: '图片地址'
       }
     * */
    @PostMapping(value="/admin_article/uploadImage")
    public Object upload(MultipartFile upload, HttpServletRequest request) {
        Map result = new HashMap();
        result.put("uploaded", 0);
        if (upload != null) {
            String folder = "img/blog";
            String path = LinkTool.getHostOf("imagePath") + "/blog";
            // 指定图片上传目录
            File imageFolder = new File(path);
            // 图片文件名+当前时间戳
            String orgFileName = upload.getOriginalFilename();
            String suffixName = ImageUtil.getSuffixName(orgFileName);
            if (suffixName != null) {
                String fileName = "blog" + new Date().getTime() + "." + suffixName;
                File file = new File(imageFolder, fileName);
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                try {
                    upload.transferTo(file);
                    result.put("uploaded", 1);
                    result.put("url", "/"+folder+ "/"+fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                String fileName = "blog" + new Date().getTime() + ".jpg";
                File file = new File(imageFolder, fileName);
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                try {
                    upload.transferTo(file);
                    BufferedImage img = ImageUtil.change2jpg(file);
                    ImageIO.write(img, "jpg", file);
                    result.put("uploaded", 1);
                    result.put("url", "/"+folder+ "/"+fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /*
     * 指定分类下的博客列表(分页)
     * */
    @GetMapping(value="/articles/articles_of_category")
    public Page4Navigator<Article> listOfCategory(@RequestParam(value = "cid", defaultValue = "0") int cid, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "10") int size) {
        start = start < 0 ? 0 : start;
        Category bean = categoryService.getById(cid);
        // 分页
        Page4Navigator<Article> page = articleService.listOfCategory(bean, start, size, 100);

        articleService.setArticleTagsAndLikesAndImg(page.getContent());
        return page;
    }

    /*
     * 获取文章时间点列表
     * */
    @GetMapping(value="/list_date")
    public Object listDate() {
        return articleService.listDate();
    }

    /*
     * 获取时间段文章数量
     * */
    @GetMapping(value="/count_by_month")
    public Object countByMonth(@RequestParam(value = "date", defaultValue = "") String date) {
        Map<String, Object> map = new HashMap<>();
        map.put("count", articleService.countByMonth(date));
        return ResultApi.success(map);
    }

    /*
     * 指定时间点下的博客列表(分页)
     * */
    @GetMapping(value="/articles/articles_of_date")
    public Page4Navigator<Article> listOfDate(@RequestParam(value = "date", defaultValue = "") String date, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "10") int size) {
        start = start < 0 ? 0 : start;
        // 分页
        Page4Navigator<Article> page = articleService.listOfDate(date, start, size, 100);

        articleService.setArticleTagsAndLikesAndImg(page.getContent());
        return page;
    }

    /*
     * 指定时间点下的博客列表(分页)
     * */
    @GetMapping(value="/articles/articles_of_tag")
    public Page4Navigator<Article> listOfTag(@RequestParam(value = "tid", defaultValue = "0") int tid, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "10") int size) {
        start = start < 0 ? 0 : start;
        if (tid <= 0) {
            return null;
        }
        // 分页
        Page4Navigator<Article> page = articleService.listOfTag(tid, start, size, 100);

        articleService.setArticleTagsAndLikesAndImg(page.getContent());
        return page;
    }

    /*
     * 文章阅读数
     * */
    @PostMapping(value="/articles/read_count")
    public Object readCounts(@RequestBody Article bean) {
        Map<String, Object> map = new HashMap<>();
        map.put("read_count", readRecordService.countByArticle(bean));
        return ResultApi.success(map);
    }

    /*
     * 文章点赞数
     * */
    @PostMapping(value="/articles/like_count")
    public Object likeCounts(@RequestBody Article bean) {
        Map<String, Object> map = new HashMap<>();
        map.put("like_count", likeRecordService.countByArticle(bean.getId()));
        return ResultApi.success(map);
    }

    /*
     * 统计博客数量
     * */
    @GetMapping(value="/articles/init_database2es")
    public Object initDatabase2ES() {
        try {
            articleService.initDatabase2ES();
            return ResultApi.success();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultApi.fail("同步失败");
    }

    /*
     * 文章搜索结果
     * */
    @GetMapping(value="/articles/search_result")
    public Page4Navigator<Article> search(@RequestParam(value = "keyword", defaultValue = "") String keyword, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "10") int size
    , @RequestParam(value = "sort", defaultValue = "1") int sort) throws IOException {
        start = start < 0 ? 0 : start;
        // 分页
        SearchHits hits = articleService.search(keyword, start, size, sort);
        long total = hits.getTotalHits().value;
        List<Article> articles = new ArrayList<>();
        for (SearchHit hit : hits) {
            JSONObject jsonObject = JSONObject.parseObject(hit.getSourceAsString());
            Map<String, HighlightField> highlightFieldMap = hit.getHighlightFields();
            Article article = JSONObject.toJavaObject(jsonObject, Article.class);
            HighlightField title = highlightFieldMap.get("title");
            if (title != null) {
                article.setTitle(title.getFragments()[0].toString());
            }
            HighlightField desc = highlightFieldMap.get("description");
            if (desc != null) {
                article.setDescription(desc.getFragments()[0].toString());
            }
            article.setViewCount(readRecordService.countByArticle(article));
            articles.add(article);
        }
        if (CollectionUtils.isEmpty(articles)) {
            return null;
        }
        if (sort == ESUtil.SORT_METHOD_POPULARITY) {
            Collections.sort(articles, new ViewCountComparator());
        }
        return new Page4Navigator<>(start+1, articles, total, size, 100);
    }


}
