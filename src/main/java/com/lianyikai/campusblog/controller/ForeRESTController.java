package com.lianyikai.campusblog.controller;

import com.lianyikai.campusblog.pojo.Article;
import com.lianyikai.campusblog.service.*;
import com.lianyikai.campusblog.utils.Page4Navigator;
import com.lianyikai.campusblog.utils.ResultApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/*
 * 前台页面访问控制器
 * */
@RestController
public class ForeRESTController {
    private static final int SHOW_COUNT = 5;

    @Autowired
    ArticleService articleService;
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    TagService tagService;
    @Autowired
    VisitRecordService visitRecordService;
    @Autowired
    ReadRecordService readRecordService;
    @Autowired
    LikeRecordService likeRecordService;

    /*
     * 首页
     * */
    @GetMapping("/foreindex")
    public Object index(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "10") int size) {
        Map<String, Object> map = new HashMap<>();
        Page4Navigator<Article> page = articleService.list(start, size, 100);
        map.put("articlePage", page);
        map.put("mostComment", articleService.listMostComment());
        map.put("mostVisit", articleService.listMostVisit());
        map.put("tags", tagService.listPubDesc());
        map.put("articlesCount", articleService.countAll());
        map.put("commentsCount", commentService.countAll());
        map.put("visitCount", visitRecordService.countAll());
        map.put("visitorCount", userService.countAll());

        Map<Integer, Integer> readCountMap = new HashMap<>();
        Map<Integer, Integer> likeCountMap = new HashMap<>();
        Map<Integer, Integer> commentCountMap = new HashMap<>();
        for (Article article : page.getContent()) {
            readCountMap.put(article.getId(), readRecordService.countByArticle(article));
            likeCountMap.put(article.getId(), likeRecordService.countByArticle(article.getId()));
            commentCountMap.put(article.getId(), commentService.countByArticle(article.getId()));
        }

        map.put("readCountMap", readCountMap);
        map.put("likeCountMap", likeCountMap);
        map.put("commentCountMap", commentCountMap);
        return ResultApi.success(map);
    }
}
