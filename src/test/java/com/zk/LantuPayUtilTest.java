package com.zk;

import com.zk.utils.LanTuPaySignUtil;

import java.math.BigDecimal;

public class LantuPayUtilTest {
    public static void main(String[] args) {
        String s = LanTuPaySignUtil.startLanTu("0.01", new BigDecimal(0.01), "xjfig", "ss@qq.com");
        System.out.println(s);
    }
}

