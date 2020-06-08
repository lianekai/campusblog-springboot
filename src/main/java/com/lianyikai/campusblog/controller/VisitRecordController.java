package com.lianyikai.campusblog.controller;

import com.lianyikai.campusblog.service.VisitRecordService;
import com.lianyikai.campusblog.utils.ResultApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/*
 * 访问记录控制器
 * */
@RestController
public class VisitRecordController {
    @Autowired
    VisitRecordService visitRecordService;

    /*
     * 访问记录数量
     * */
    @GetMapping(value="/count_visit_all")
    public Object countVisitAll() {
        Map<String, Object> map = new HashMap<>();
        map.put("countAll", visitRecordService.countAll());
        return ResultApi.success(map);
    }

    /*
     * 统计昨日访问
     * */
    @GetMapping(value="/count_visit_yesterday")
    public Object countYesterdayVisit() {
        Map<String, Object> map = new HashMap<>();
        map.put("countYesterday", visitRecordService.countYesterday());
        return ResultApi.success(map);
    }
}
