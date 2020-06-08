package com.lianyikai.campusblog.controller;

import com.lianyikai.campusblog.service.LikeRecordService;
import com.lianyikai.campusblog.service.UserService;
import com.lianyikai.campusblog.pojo.Comment;
import com.lianyikai.campusblog.pojo.LikeRecord;
import com.lianyikai.campusblog.pojo.User;
import com.lianyikai.campusblog.service.CommentService;
import com.lianyikai.campusblog.utils.Page4Navigator;
import com.lianyikai.campusblog.utils.ResultApi;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/*
 * 动态控制器
 * */
@RestController
public class FeedController {
    @Autowired
    CommentService commentService;
    @Autowired
    LikeRecordService likeRecordService;
    @Autowired
    UserService userService;

    /*
     * 动态消息数量
     * */
    @GetMapping(value="/feed/count_all")
    public Object countAll() {
        Subject subject = SecurityUtils.getSubject();
        User user;
        if (subject.isAuthenticated() || subject.isRemembered()) {
            user = userService.getByName(subject.getPrincipal().toString());
            int countComment = commentService.countByUser(user);
            int countLike = likeRecordService.countByUser(user);
            Map<String, Integer> map = new HashMap<>();
            map.put("feedCount", countComment + countLike);
            return ResultApi.success(map);
        }
        return null;
    }

    @GetMapping(value="/feed/activity_of_page")
    public Object activity(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "15") int size) {
        Subject subject = SecurityUtils.getSubject();
        User user;
        if (subject.isAuthenticated() || subject.isRemembered()) {
            user = userService.getByName(subject.getPrincipal().toString());
            start = start < 0 ? 0 : start;
            // 分页
            Page4Navigator<Comment> comments = commentService.listOfUser(start, size, user, 100);
            commentService.setCommentParent(comments.getContent());
            commentService.setLikeCount(comments.getContent());
            Page4Navigator<LikeRecord> likes = likeRecordService.listByUser(start, size, user, 100);
            Map<String, Object> map = new HashMap<>();
            map.put("comments", comments);
            map.put("likes", likes);
            return map;
        }
        return null;
    }

    @GetMapping(value="/feed/message_of_page")
    public Object message(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "15") int size) {
        Subject subject = SecurityUtils.getSubject();
        User user;
        if (subject.isAuthenticated() || subject.isRemembered()) {
            user = userService.getByName(subject.getPrincipal().toString());
            start = start < 0 ? 0 : start;
            // 分页
            Page4Navigator<Comment> page = commentService.listMessageOfUser(start, size, user, 100);
            commentService.setCommentParent(page.getContent());
            commentService.setLikeCount(page.getContent());
            return page;
        }
        return null;
    }

    /*
     * 动态消息数量
     * */
    @GetMapping(value="/feed/count_message_all")
    public Object countAllMessage() {
        Subject subject = SecurityUtils.getSubject();
        User user;
        if (subject.isAuthenticated() || subject.isRemembered()) {
            user = userService.getByName(subject.getPrincipal().toString());
            int countMessage = commentService.countMessageByUser(user);
            Map<String, Integer> map = new HashMap<>();
            map.put("feedCount", countMessage);
            return ResultApi.success(map);
        }
        return null;
    }

    /*
     * 标记全部消息已读
     * */
    @GetMapping(value="/feed/all_message_read")
    public Object allMessageRead() {
        Subject subject = SecurityUtils.getSubject();
        User user;
        if (subject.isAuthenticated() || subject.isRemembered()) {
            user = userService.getByName(subject.getPrincipal().toString());
            commentService.allMessageRead(user.getId());
            return ResultApi.success();
        }
        return ResultApi.fail("操作失败!");
    }
}
