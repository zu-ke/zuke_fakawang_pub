package com.zk.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zk.bean.Admin;
import com.zk.bean.Result;
import com.zk.service.AdminService;
import com.zk.utils.fakaUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Resource
    private AdminService adminService;

    @Value("${zk.yqm}")
    private String yqm;

    @GetMapping(value = {"/"})
    public String goToUser(){
        return "index";
    }

    @GetMapping(value = {"/login"})
    public String goToLogin(){
        return "index";
    }

    @PostMapping("/admin/login")
    @ResponseBody
    public Result login(@Validated  @RequestBody Admin admin, HttpSession session, Errors errors) {
        StringBuilder validate = fakaUtils.validate(errors);
        if (validate.length() > 0) {
            return Result.error("1001", validate.toString());
        }

        QueryWrapper<Admin> queryWrapper = Wrappers.query();
        queryWrapper.eq("username", admin.getUsername());
        queryWrapper.eq("password", fakaUtils.encrypt(admin.getPassword()));
        Admin one = adminService.getOne(queryWrapper);
        if (one != null) {
            session.setAttribute("username", admin.getUsername());
            return Result.success();
        }
        return Result.error("1001", "账号或者密码错误", null);
    }

    @PostMapping("/admin/register")
    @ResponseBody
    public Result register(@Validated @RequestBody Admin admin, Errors errors) {
        StringBuilder validate = fakaUtils.validate(errors);
        if (validate.length() > 0) {
            return Result.error("1001", validate.toString());
        }

        if (!StringUtils.hasText(admin.getYqm()) || !admin.getYqm().equals(yqm)) {
            return Result.error("1001", "注册失败，请填写正确邀请码");
        }
        boolean res = adminService.save(new Admin(admin.getUsername(), fakaUtils.encrypt(admin.getPassword()), admin.getYqm()));
        if (res) {
            return Result.success();
        }
        return Result.error("1001", "失败，未知错误");
    }
}
