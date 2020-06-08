package com.lianyikai.campusblog.controller;

import com.lianyikai.campusblog.service.UserService;
import com.lianyikai.campusblog.pojo.Comment;
import com.lianyikai.campusblog.pojo.CommentLikeRecord;
import com.lianyikai.campusblog.pojo.User;
import com.lianyikai.campusblog.service.CommentLikeService;
import com.lianyikai.campusblog.service.CommentService;
import com.lianyikai.campusblog.utils.CookieUtils;
import com.lianyikai.campusblog.utils.Page4Navigator;
import com.lianyikai.campusblog.utils.ResultApi;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * 评论控制器
 * */
@RestController
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    CommentLikeService commentLikeService;

    /*
     * 评论列表(根据发布时间倒排序)
     * */
    @GetMapping(value="/admin_comment/comments")
    public Object findAll() {
        return commentService.listOrderPubDesc();
    }

    /*
     * 统计评论数量
     * */
    @GetMapping(value="/admin_comment/count_all")
    public Object countAll() {
        Map<String, Object> map = new HashMap<>();
        map.put("countAll", commentService.countAll());
        return ResultApi.success(map);
    }

    /*
     * 统计未读评论数量
     * */
    @GetMapping(value="/admin_comment/count_no_read")
    public Object countNoRead() {
        Map<String, Object> map = new HashMap<>();
        map.put("countNoRead", commentService.countNoRead());
        return ResultApi.success(map);
    }

    /*
     * 评论列表(分页)
     * */
    @GetMapping(value="/admin_comment/comments_of_page")
    public Page4Navigator<Comment> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "10") int size) {
        start = start < 0 ? 0 : start;
        // 分页
        Page4Navigator<Comment> page = commentService.list(start, size, 10);
        commentService.setCommentParent(page.getContent());
        return page;
    }

    /*
     * 评论
     * */
    @PostMapping(value="/comments")
    public Object add(@RequestBody Comment bean) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() || subject.isRemembered()) {
            User user = userService.getByName(subject.getPrincipal().toString());
            bean.setAnswerer(user);
            bean.setCreatedAt(new Date());
            commentService.setCommentParent(bean);
            if (bean.getParent() != null) {
                bean.setRespondent(bean.getParent().getAnswerer());
            }
            commentService.add(bean);
        } else {
            bean.setId(0);
        }
        return bean;
    }

    /*
     * 删除评论
     * */
    @DeleteMapping("/comments/{id}")
    public Object delete(@PathVariable("id") int id, HttpServletRequest request) {
        commentService.delete(id);
        return ResultApi.success();
    }

    /*
     * 标记全部评论已读
     * */
    @GetMapping(value="/admin_comment/all_of_read")
    public Object allOfRead() {
        commentService.allOfRead();
        return ResultApi.success();
    }

    /*
     * 统计文章评论数
     * */
    @GetMapping("/comments/count_by_article/{id}")
    public Object countByArticle(@PathVariable("id") int id, HttpSession session, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("count", commentService.countByArticle(id));
        User user = (User) session.getAttribute("user");
        if (user != null) {
            map.put("loginUser", user.getId());
        } else {
            String name = CookieUtils.getCookie(request, UserService.JSESSIONID);
            User foundUser = userService.getByName(name);
            if (foundUser != null && foundUser.getId() > 0) {
                map.put("loginUser", foundUser.getId());
            } else {
                map.put("loginUser", 0);
            }
        }
        return ResultApi.success(map);
    }

    /*
     * 文章详情评论列表(分页)
     * */
    @GetMapping(value="/comments/list")
    public Page4Navigator<Comment> list4Article(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "10") int size,
                                                @RequestParam(value = "aid", defaultValue = "0") int aid) {
        start = start < 0 ? 0 : start;
        // 分页
        Page4Navigator<Comment> page = commentService.list4Article(aid, start, size, 5);
        commentService.setCommentParent(page.getContent());
        commentService.setLikeCount(page.getContent());
        return page;
    }

    /*
     * 点赞评论
     * */
    @PostMapping(value="/comments/fun")
    public Object fun(@RequestBody CommentLikeRecord bean) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() || subject.isRemembered()) {
            User user = userService.getByName(subject.getPrincipal().toString());
            bean.setUser(user);
            bean.setCreatedAt(new Date());
            commentLikeService.add(bean);
        } else {
            bean.setId(0);
        }
        return bean;
    }

    /*
     * 取消点赞
     * */
    @PostMapping(value="/comments/not_fun")
    public Object notFun(@RequestBody CommentLikeRecord bean) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() || subject.isRemembered()) {
            User user = userService.getByName(subject.getPrincipal().toString());
            commentLikeService.delete(user.getId(), bean.getComment().getId());
            bean.setId(1);
        }
        return bean;
    }

    /*
     * 用户已点赞该评论
     * */
    @GetMapping("/comments/is_liked")
    public Object isLiked(@RequestParam(value = "cid", defaultValue = "0") int cid) {
        Subject subject = SecurityUtils.getSubject();
        boolean isLiked = false;
        if (subject.isAuthenticated()) {
            User user = userService.getByName(subject.getPrincipal().toString());
            isLiked = commentLikeService.isLiked(user.getId(), cid);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("liked", isLiked);
        return ResultApi.success(map);
    }
}
