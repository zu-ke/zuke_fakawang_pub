package com.zk.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zk.bean.Category;
import com.zk.bean.Product;
import com.zk.mapper.CategoryMapper;
import com.zk.mapper.ProductMapper;
import com.zk.service.CategoryService;
import com.zk.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
}
