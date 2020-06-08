package com.lianyikai.campusblog.controller;

import com.lianyikai.campusblog.pojo.User;
import com.lianyikai.campusblog.service.UserService;
import com.lianyikai.campusblog.utils.ImageUtil;
import com.lianyikai.campusblog.utils.LinkTool;
import com.lianyikai.campusblog.utils.Page4Navigator;
import com.lianyikai.campusblog.utils.ResultApi;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * 用户控制器
 * */
@RestController
public class UserController {
    @Autowired
    UserService userService;

    /*
     * 用户数量
     * */
    @GetMapping(value="/count_user_all")
    public Object countUserAll() {
        Map<String, Object> map = new HashMap<>();
        map.put("countUserAll", userService.countAll());
        return ResultApi.success(map);
    }

    /*
     * 用户列表(分页)
     * */
    @GetMapping(value="/admin_user/users")
    public Page4Navigator<User> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "15") int size) {
        start = start < 0 ? 0 : start;
        // 分页
        Page4Navigator<User> page = userService.list(start, size, 100);
        return page;
    }

    /*
     * 屏蔽，注销操作
     * */
    @PutMapping("/users")
    public Object update(@RequestBody User bean) {
        User user = userService.getByName(bean.getUsername());
        user.setStatus(bean.getStatus());
        userService.save(user);
        return user;
    }

    /*
     * 更新个人信息
     * */
    @PutMapping("/user/update")
    public Object updateInfo(@RequestBody User bean) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        bean.setBirthday(sdf.parse(bean.getBirthdayStr()));
        userService.save(bean);
        return bean;
    }

    /*
     * 更新个人信息
     * */
    @PostMapping("/user/upload")
    public Object upload(MultipartFile upload) {
        Map<String, Object> map = new HashMap<>();
        map.put("uploaded", 0);
        if (upload != null) {
            String path = LinkTool.getHostOf("imagePath") + "/face";
            // 指定图片上传目录
            File imageFolder = new File(path);
            // 图片文件名+当前时间戳
            String orgFileName = upload.getOriginalFilename();
            String suffixName = ImageUtil.getSuffixName(orgFileName);
            if (suffixName != null) {
                String fileName = "face" + new Date().getTime() + "." + suffixName;
                File file = new File(imageFolder, fileName);
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                try {
                    upload.transferTo(file);
                    ImageUtil.resizeImage(file, 180, 180, suffixName);
                    map.put("uploaded", 1);
                    map.put("image", "img/face/"+fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                String fileName = "face" + new Date().getTime() + ".jpg";
                File file = new File(imageFolder, fileName);
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                try {
                    upload.transferTo(file);
                    ImageUtil.resizeImage(file, 120, 120, "jpg");
                    map.put("uploaded", 1);
                    map.put("image", "img/face/"+fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    /*
     * 授权操作
     * */
    @PutMapping("/users/authorize")
    public Object authorize(@RequestBody User bean) {
        userService.relateUserRole(bean);
        return bean;
    }

    /*
     * 注册
     * */
    @PostMapping(value="/users")
    public Object register(@RequestBody User user) {
        String name =  user.getUsername();
        String password = user.getPassword();
        name = HtmlUtils.htmlEscape(name);
        user.setUsername(name);
        boolean exist = userService.isExist(name);
        if(exist){
            String message ="该用户已存在";
            return ResultApi.fail(message);
        }
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String algorithmName = "md5";
        String encodedPassword = new SimpleHash(algorithmName, password, salt, times).toString();
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        user.setRegisterTime(new Date());
        user.setStatus(User.STATUS_NORMAL);
        user.setType(User.TYPE_REGISTER);
        user.setFace("/img/face/default.jpg");
        userService.register(user);
        return ResultApi.success();
    }

    /*
     * 登录
     * */
    @PostMapping("/login")
    public Object login(@RequestBody User userParam, @RequestParam(value = "rememberMe") boolean rememberMe, HttpSession session) {
        String name =  userParam.getUsername();
        name = HtmlUtils.htmlEscape(name);

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, userParam.getPassword(), rememberMe);
        try {
            subject.login(token);
            User user = userService.getByName(name);
            session.setAttribute("user", user);
            session.setAttribute("role", userService.getRoleByUser(user.getId()));

            return ResultApi.success();
        } catch (AuthenticationException e) {
            String message ="昵称或密码错误!";
            return ResultApi.fail(message);
        }
    }

    /*
     * 个人信息
     * */
    @GetMapping("/user/info")
    public User info() {
        Subject subject = SecurityUtils.getSubject();
        User user = null;
        if (subject.isAuthenticated() || subject.isRemembered()) {
            user = userService.getByName(subject.getPrincipal().toString());
            if (user.getBirthday() != null)
                user.setBirthdayStr(user.getBirthday().toString().substring(0, 10));
        }
        return user;
    }
}
