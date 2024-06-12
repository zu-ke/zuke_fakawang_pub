package com.zk.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Date;

/*解读：公告表*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Announcement {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @NotEmpty(message = "公告不能为空")
    private String content;
    private Date createTime;
    private Date updateTime;
}

