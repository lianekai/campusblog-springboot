package com.lianyikai.campusblog.controller;

import com.lianyikai.campusblog.pojo.FeedBack;
import com.lianyikai.campusblog.service.FeedBackService;
import com.lianyikai.campusblog.utils.Page4Navigator;
import com.lianyikai.campusblog.utils.ResultApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/*
 * 反馈控制器
 * */
@RestController
public class FeedBackController {
    @Autowired
    FeedBackService feedBackService;

    /*
     * 反馈列表(根据发布时间倒排序)
     * */
    @GetMapping(value="/admin_feedback/feedbacks")
    public Object findAll() {
        return feedBackService.listPubDesc();
    }

    /*
     * 反馈数量
     * */
    @GetMapping(value="/admin_feedback/count_all")
    public Object countAll() {
        Map<String, Object> map = new HashMap<>();
        map.put("countAll", feedBackService.countAll());
        return ResultApi.success(map);
    }

    /*
     * 反馈列表(分页)
     * */
    @GetMapping(value="/admin_feedback/feedbacks_of_page")
    public Page4Navigator<FeedBack> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) {
        start = start < 0 ? 0 : start;
        // 分页
        Page4Navigator<FeedBack> page = feedBackService.list(start, size, 5);

        return page;
    }


    /*
     * 删除反馈
     * */
    @DeleteMapping("/feedbacks/{id}")
    public Object delete(@PathVariable("id") int id, HttpServletRequest request) {
        feedBackService.delete(id);
        return ResultApi.success();
    }
}
