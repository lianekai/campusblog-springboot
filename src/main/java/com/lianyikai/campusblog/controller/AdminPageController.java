package com.lianyikai.campusblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
* 后台管理页面跳转控制器
* */
@Controller
public class AdminPageController {
    @GetMapping(value="/admin")
    public String index(){
        return "admin/admin";
    }
    @GetMapping(value="/admin_article")
    public String article(){
        return "admin/article";
    }
    @GetMapping(value="/admin_write_article")
    public String writeArticle() {
        return "admin/write_article";
    }
    @GetMapping(value="/admin_edit_article")
    public String editArticle() {
        return "admin/edit_article";
    }
    @GetMapping(value="/admin_like")
    public String like() {
        return "admin/like";
    }
    @GetMapping(value="/admin_category")
    public String category() {
        return "admin/category";
    }
    @GetMapping(value="/admin_tag")
    public String tag() {
        return "admin/tag";
    }
    @GetMapping(value="/admin_link")
    public String link() {
        return "admin/link";
    }
    @GetMapping(value="/admin_feedback")
    public String feedback() {
        return "admin/feedback";
    }
    @GetMapping(value="/admin_message")
    public String message() {
        return "admin/message";
    }
    @GetMapping(value="/admin_comment")
    public String comment() {
        return "admin/comment";
    }
    @GetMapping(value="/admin_user")
    public String user() {
        return "admin/user";
    }
}
