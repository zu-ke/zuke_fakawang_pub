package com.zk.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/*解读：商品表*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @NotNull(message = "商品分类不能为空")
    private Integer categoryId;
    private String categoryName;
    @NotEmpty(message = "商品名称不能为空")
    private String name;
    @NotNull(message = "请输入数字")
    @Range(min = 0, message = "价格不能小于0")
    private BigDecimal price;
    private Date createTime;
    private Date updateTime;
    @NotEmpty(message = "卡密不能为空")
    private String gptKey;
}

