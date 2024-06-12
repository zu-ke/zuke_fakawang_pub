package com.zk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zk.bean.Order_;
import com.zk.bean.Product;
import com.zk.bean.Result;
import com.zk.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Controller
public class OrderController {

    @Resource
    private OrderService orderService;

    @GetMapping("/admin/listO")
    @ResponseBody
    public Result listO(@RequestParam("pageSize") Integer pageSize,
                                 @RequestParam("pageNum") Integer pageNum,
                                 @RequestParam(value = "searchName", defaultValue = "") String searchName) {
        LambdaQueryWrapper<Order_> lambdaQueryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.hasLength(searchName)) {
            lambdaQueryWrapper.like(Order_::getName, searchName);
        }
        Page<Order_> page = orderService.page(new Page<>(pageNum, pageSize), lambdaQueryWrapper);
        return Result.success(page);
    }
}
