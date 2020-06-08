package com.lianyikai.campusblog.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * 前台页面跳转控制器
 * */
@Controller
public class ForePageController {
    @GetMapping(value="/")
    public String index(){
        return "redirect:index";
    }
    @GetMapping(value="/index")
    public String home(){
        return "/fore/index";
    }
    @GetMapping(value="/article")
    public String articleDetail(){
        return "/fore/show";
    }
    @GetMapping(value="/register")
    public String register(){
        return "/fore/register";
    }
    @GetMapping(value="/login")
    public String login() {
        return "fore/login";
    }
    @GetMapping(value="/page404")
    public String page404() {
        return "error/404";
    }
    @GetMapping(value="/page500")
    public String page500() {
        return "error/500";
    }
    @GetMapping(value="/categories")
    public String categories() {
        return "fore/categories";
    }
    @GetMapping(value="/date")
    public String date() {
        return "fore/date";
    }
    @GetMapping(value="/tags")
    public String tags() {
        return "fore/tags";
    }
    @GetMapping(value="/article_of_tag")
    public String articleOfTag() {
        return "fore/article_of_tag";
    }
    @GetMapping(value="/friendlylink")
    public String friendLink() {
        return "fore/link";
    }
    @GetMapping(value="/search")
    public String search() {
        return "fore/search";
    }
    @GetMapping(value="/activity")
    public String activity() {
        return "fore/activity";
    }
    @GetMapping(value="/message")
    public String message() {
        return "fore/message";
    }
    @GetMapping(value="/home")
    public String page(){
        return "/fore/home";
    }
    @GetMapping("/logout")
    public String logout( ) {
        Subject subject = SecurityUtils.getSubject();
        if(subject.isRemembered() || subject.isAuthenticated())
            subject.logout();
        return "redirect:index";
    }
}
