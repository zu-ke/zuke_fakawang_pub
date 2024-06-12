package com.zk.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zk.bean.Category;
import com.zk.bean.Product;
import com.zk.bean.Result;
import com.zk.service.CategoryService;
import com.zk.service.ProductService;
import com.zk.utils.fakaUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Controller
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @Resource
    private ProductService productService;

    @PostMapping("/admin/saveCtg")
    @ResponseBody
    public Result saveCtg(@Validated @RequestBody Category category, Errors errors) {
        StringBuilder validate = fakaUtils.validate(errors);
        if (validate.length() > 0) {
            return Result.error("1001", validate.toString());
        }
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());
        category.setStock(0);
        boolean save = categoryService.save(category);
        if (!save) {
            return Result.error("1001", "失败，未知错误");
        }
        return Result.success();
    }

    @PostMapping("/admin/delCtg")
    @ResponseBody
    public Result saveDel(@RequestParam("cid") String cid) {
        if (!StringUtils.hasText(cid)) {
            return Result.error("1001", "未能确定需要删除的分类");
        }
        boolean save = categoryService.removeById(cid);
        QueryWrapper<Product> queryWrapper = Wrappers.query();
        queryWrapper.eq("category_id", cid);
        productService.remove(queryWrapper);
        if (!save) {
            return Result.error("1001", "失败，未知错误");
        }
        return Result.success();
    }

    @PostMapping("/admin/upCtg")
    @ResponseBody
    public Result saveUp(@Validated @RequestBody Category category, Errors errors) {
        StringBuilder validate = fakaUtils.validate(errors);
        if (validate.length() > 0) {
            return Result.error("1001", validate.toString());
        }
        category.setUpdateTime(new Date());
        boolean save = categoryService.saveOrUpdate(category);
        if (!save) {
            return Result.error("1001", "失败，未知错误");
        }
        return Result.success();
    }

    @GetMapping("/admin/listCtg")
    @ResponseBody
    public Result listCtg() {
        List<Category> list = categoryService.list();
        return Result.success(list);
    }

    @GetMapping("/admin/getByIdCtg")
    @ResponseBody
    public Result getByIdCtg(@RequestParam("cid") String cid) {
        if (!StringUtils.hasText(cid)) {
            return Result.error("1001", "未能确定需要删除的分类");
        }
        Category category = categoryService.getById(cid);
        return Result.success(category);
    }
}
