package com.lianyikai.campusblog.controller;

import com.lianyikai.campusblog.pojo.Category;
import com.lianyikai.campusblog.service.CategoryService;
import com.lianyikai.campusblog.utils.Page4Navigator;
import com.lianyikai.campusblog.utils.ResultApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * 分类控制器
 * */
@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    /*
     * 分类列表(发布时间倒排序)
     * */
    @GetMapping(value="/all_categories")
    public Object findAll() {
        return categoryService.listOrderPubDesc();
    }

    /*
     * 分类数量
     * */
    @GetMapping(value="/admin_category/count_all")
    public Object countAll() {
        Map<String, Object> map = new HashMap<>();
        map.put("countAll", categoryService.countAll());
        return ResultApi.success(map);
    }

    /*
     * 分类列表(分页)
     * */
    @GetMapping(value="/admin_category/categories_of_page")
    public Page4Navigator<Category> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "20") int size) {
        start = start < 0 ? 0 : start;
        Page4Navigator<Category> page = categoryService.list(start, size, 5);

        return page;
    }

    /*
     * 添加分类
     * */
    @PostMapping(value="/categories")
    public Object add(@RequestParam(value = "name", defaultValue = "") String name) {
        Category bean = new Category();
        if (categoryService.isExist(name)) {
            bean.setId(-1);
            return bean;
        }
        bean.setName(name);
        bean.setCreatedAt(new Date());
        categoryService.add(bean);
        return bean;
    }

    /*
     * 删除分类
     * */
    @DeleteMapping("/categories/{id}")
    public Object delete(@PathVariable("id") int id, HttpServletRequest request) {
        categoryService.delete(id);
        return ResultApi.success();
    }
}
