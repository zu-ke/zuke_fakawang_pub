package com.zk.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/*解读：管理员表*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {

    @TableId(type = IdType.AUTO)
    private Integer id;
    @NotEmpty(message = "账号不能未空")
    private String username;
    @NotEmpty(message = "密码不能为空")
    private String password;
    private Date updateTime = new Date();
    private String yqm;

    public Admin(String username, String password, String yqm) {
        this.username = username;
        this.password = password;
        this.yqm = yqm;
    }
}
