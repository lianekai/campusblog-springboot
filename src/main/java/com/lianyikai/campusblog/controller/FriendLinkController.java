package com.lianyikai.campusblog.controller;

import com.lianyikai.campusblog.pojo.FriendLink;
import com.lianyikai.campusblog.service.FriendLinkService;
import com.lianyikai.campusblog.utils.Page4Navigator;
import com.lianyikai.campusblog.utils.ResultApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * 友链控制器
 * */
@RestController
public class FriendLinkController {
    @Autowired
    FriendLinkService friendLinkService;

    /*
     * 友链列表(根据发布时间倒排序)
     * */
    @GetMapping(value="/admin_link/links")
    public Object findAll() {
        return friendLinkService.listPubDesc();
    }

    /*
     * 友链数量
     * */
    @GetMapping(value="/admin_link/count_all")
    public Object countAll() {
        Map<String, Object> map = new HashMap<>();
        map.put("countAll", friendLinkService.countAll());
        return ResultApi.success(map);
    }

    /*
     * 友链列表(分页)
     * */
    @GetMapping(value="/links_of_page")
    public Page4Navigator<FriendLink> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "20") int size) {
        start = start < 0 ? 0 : start;
        // 分页
        Page4Navigator<FriendLink> page = friendLinkService.list(start, size, 5);

        return page;
    }

    /*
     * 添加友链
     * */
    @PostMapping(value="/links")
    public Object add(@RequestParam(value = "blogger", defaultValue = "") String blogger, @RequestParam(value = "url", defaultValue = "") String url) {
        FriendLink bean = new FriendLink();
        bean.setBlogger(blogger);
        bean.setUrl(url);
        bean.setCreatedAt(new Date());
        friendLinkService.add(bean);
        return bean;
    }

    /*
     * 删除友链
     * */
    @DeleteMapping("/links/{id}")
    public Object delete(@PathVariable("id") int id, HttpServletRequest request) {
        friendLinkService.delete(id);
        return ResultApi.success();
    }

    /*
     * 通过id获取友链
     * */
    @GetMapping("/links/{id}")
    public FriendLink get(@PathVariable("id") int id) {
        return friendLinkService.getById(id);
    }

    /*
     * 编辑列表
     * */
    @PutMapping("/links")
    public Object update(@RequestBody FriendLink bean) {
        friendLinkService.update(bean);
        return bean;
    }
}
