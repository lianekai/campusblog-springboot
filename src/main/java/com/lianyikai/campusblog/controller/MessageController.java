package com.lianyikai.campusblog.controller;

import com.lianyikai.campusblog.pojo.Message;
import com.lianyikai.campusblog.service.MessageService;
import com.lianyikai.campusblog.utils.Page4Navigator;
import com.lianyikai.campusblog.utils.ResultApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/*
 * 留言控制器
 * */
@RestController
public class MessageController {
    @Autowired
    MessageService messageService;

    /*
     * 留言列表(根据发布时间倒排序)
     * */
    @GetMapping(value="/admin_message/messages")
    public Object findAll() {
        return messageService.listOrderPubDesc();
    }

    /*
     * 留言数量
     * */
    @GetMapping(value="/admin_message/count_all")
    public Object countAll() {
        Map<String, Object> map = new HashMap<>();
        map.put("countAll", messageService.countAll());
        return ResultApi.success(map);
    }

    /*
     * 留言列表(分页)
     * */
    @GetMapping(value="/admin_message/messages_of_page")
    public Page4Navigator<Message> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) {
        start = start < 0 ? 0 : start;
        Page4Navigator<Message> page = messageService.list(start, size, 5);

        return page;
    }


    /*
     * 删除留言
     * */
    @DeleteMapping("/messages/{id}")
    public Object delete(@PathVariable("id") int id, HttpServletRequest request) {
        messageService.delete(id);
        return ResultApi.success();
    }
}
