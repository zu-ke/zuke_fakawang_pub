package com.zk.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/*解读：订单表*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order_ {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String productId;
    @Email(message = "请正确填写邮箱")
    @NotEmpty(message = "邮箱不能为空")
    private String name; //商品名称，这里直接写邮箱，商品根据amount来确定
    private String status; //0未支付 1已支付
    @NotNull(message = "请输入数字")
    @Range(min = 0, message = "价格不能小于0")
    private BigDecimal amount;
    private Date createTime;
    private Date updateTime;
    private String tradeNo; //订单号
    private String outTradeNo; //商户订单号
    private String tradeStatus; //支付状态，如果是TRADE_SUCCESS则代表成功支付
}
