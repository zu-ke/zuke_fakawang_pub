package com.zk.controller;

import com.zk.bean.Announcement;
import com.zk.bean.Result;
import com.zk.service.AnnouncementService;
import com.zk.utils.fakaUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

@Controller
public class AnnouncementController {

    @Resource
    private AnnouncementService announcementService;

    @PostMapping("/admin/saveAncmt")
    @ResponseBody
    public Result saveAncmt(@Validated @RequestBody Announcement announcement, Errors errors) {
        StringBuilder validate = fakaUtils.validate(errors);
        if (validate.length() > 0) {
            return Result.error("1001", validate.toString());
        }
        announcement.setCreateTime(new Date());
        announcement.setUpdateTime(new Date());
        announcementService.save(announcement);
        return Result.success();
    }

    @PostMapping("/admin/upAncmt")
    @ResponseBody
    public Result upAncmt(@Validated @RequestBody Announcement announcement, Errors errors) {
        StringBuilder validate = fakaUtils.validate(errors);
        if (validate.length() > 0) {
            return Result.error("1001", validate.toString());
        }
        announcement.setUpdateTime(new Date());
        announcementService.saveOrUpdate(announcement);
        return Result.success();
    }

    @GetMapping("/admin/getAncmt")
    @ResponseBody
    public Result getAncmt() {
        Announcement announcement = announcementService.getById("1");
        return Result.success(announcement);
    }

}
