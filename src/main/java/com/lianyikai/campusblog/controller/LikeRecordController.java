package com.lianyikai.campusblog.controller;

import com.lianyikai.campusblog.pojo.LikeRecord;
import com.lianyikai.campusblog.pojo.User;
import com.lianyikai.campusblog.service.LikeRecordService;
import com.lianyikai.campusblog.service.UserService;
import com.lianyikai.campusblog.utils.Page4Navigator;
import com.lianyikai.campusblog.utils.ResultApi;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * 喜欢控制器
 * */
@RestController
public class LikeRecordController {
    @Autowired
    LikeRecordService likeRecordService;
    @Autowired
    UserService userService;

    /*
     * 未读消息数量
     * */
    @GetMapping(value="/admin_like/count_not_read")
    public Object countNotRead() {
        Map<String, Object> map = new HashMap<>();
        map.put("notRead", likeRecordService.countNotRead());
        return ResultApi.success(map);
    }

    /*
     * 留言列表(根据发布时间倒排序)
     * */
    @GetMapping(value="/admin_like/likes")
    public Page4Navigator<LikeRecord> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) {
        start = start < 0 ? 0 : start;
        Page4Navigator<LikeRecord> page = likeRecordService.list(start, size, 5);

        return page;
    }

    @GetMapping(value="/admin_like/all_of_read")
    public Object allOfRead() {
        likeRecordService.allOfRead();
        return ResultApi.success();
    }

    @PostMapping(value="/likes")
    public Object add(@RequestBody LikeRecord bean) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() || subject.isRemembered()) {
            User user = userService.getByName(subject.getPrincipal().toString());
            boolean isLiked = likeRecordService.isLiked(user.getId(), bean.getArticle().getId());
            if (!isLiked) {
                bean.setUser(user);
                bean.setLikeDate(new Date());
                likeRecordService.add(bean);
            }
        }
        return bean;
    }

    /*
     * 统计文章点赞数
     * */
    @GetMapping("/likes/count_by_article/{id}")
    public Object countByArticle(@PathVariable("id") int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("count", likeRecordService.countByArticle(id));
        return ResultApi.success(map);
    }

    /*
     * 用户是否已经点赞这篇文章
     * */
    @GetMapping("/likes/is_liked")
    public Object isLiked(@RequestParam(value = "aid", defaultValue = "0") int aid) {
        Subject subject = SecurityUtils.getSubject();
        boolean isLiked = false;
        if (subject.isAuthenticated() || subject.isRemembered()) {
            User user = userService.getByName(subject.getPrincipal().toString());
            isLiked = likeRecordService.isLiked(user.getId(), aid);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("liked", isLiked);
        return ResultApi.success(map);
    }
}
