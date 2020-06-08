package com.lianyikai.campusblog.controller;

import com.lianyikai.campusblog.pojo.Tag;
import com.lianyikai.campusblog.service.TagService;
import com.lianyikai.campusblog.utils.Page4Navigator;
import com.lianyikai.campusblog.utils.ResultApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 标签控制器
 * */
@RestController
public class TagController {
    @Autowired
    TagService tagService;

    /*
     * 标签列表(根据发布时间倒排序)
     * */
    @GetMapping(value="/admin_tag/tags")
    public Object findAll() {
        List<Tag> tags = tagService.listPubDesc();
        tagService.setArticleCount(tags);
        return tags;
    }

    /*
     * 标签数量
     * */
    @GetMapping(value="/tags/count_all")
    public Object countAll() {
        Map<String, Object> map = new HashMap<>();
        map.put("countAll", tagService.countAll());
        return ResultApi.success(map);
    }

    /*
     * 标签列表(分页)
     * */
    @GetMapping(value="/tags_of_page")
    public Page4Navigator<Tag> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "20") int size) {
        start = start < 0 ? 0 : start;
        Page4Navigator<Tag> page = tagService.list(start, size, 5);
        tagService.setArticleCount(page.getContent());
        return page;
    }

    /*
     * 添加标签
     * */
    @PostMapping(value="/tags")
    public Object add(@RequestParam(value = "name", defaultValue = "") String name) {
        Tag bean = new Tag();
        if (tagService.isExist(name)) {
            bean.setId(-1);
            return bean;
        }
        bean.setName(name);
        bean.setCreatedAt(new Date());
        tagService.add(bean);
        return bean;
    }

    /*
     * 删除标签
     * */
    @DeleteMapping("/tags/{id}")
    public Object delete(@PathVariable("id") int id, HttpServletRequest request) {
        tagService.delete(id);
        return ResultApi.success();
    }

    /*
     * 当前标签文章数量
     * */
    @GetMapping(value="/tags/count_article/{id}")
    public Object countArticle(@PathVariable("id") int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("countArticle", tagService.countArticle(id));
        return ResultApi.success(map);
    }

    /*
     * 获取Tag bean
     * */
    @GetMapping("/tags/{id}")
    public Tag get(@PathVariable("id") int id) {
        Tag tag = tagService.getById(id);
        if (tag == null)
            return null;
        tagService.setArticleCount(tag);
        return tag;
    }
}
