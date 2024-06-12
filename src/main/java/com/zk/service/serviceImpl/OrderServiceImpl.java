package com.zk.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zk.bean.Order_;
import com.zk.bean.Order_;
import com.zk.bean.Product;
import com.zk.mapper.OrderMapper;
import com.zk.mapper.ProductMapper;
import com.zk.service.OrderService;
import com.zk.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order_> implements OrderService {
}
