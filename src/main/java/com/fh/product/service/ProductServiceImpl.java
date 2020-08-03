package com.fh.product.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.common.ServerResponse;
import com.fh.product.mapper.ProductMapper;
import com.fh.product.model.Product;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Override
    public ServerResponse queryHostList() {
        QueryWrapper<Product> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("isHot",1);
        List<Product> list = productMapper.selectList(queryWrapper);
        return ServerResponse.success(list);
    }

    @Override
    public ServerResponse queryProductList() {
     List<Product> list=productMapper.selectList(null);
        return ServerResponse.success(list);
    }

    @Override
    public ServerResponse queryProductListPage(long currentPage,long pageSize) {
        long start =(currentPage-1)*pageSize;

        long totalCount = productMapper.queryTotalCount();

        List<Product> list =productMapper.queryList(start,pageSize);
        long totalPage =totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1;

        Map a=new HashMap<>();
        a.put("list",list);
        a.put("totalPage",totalPage);

        /*IPage<Product> iPage=new Page(currentPage,pageSize);
        IPage<Product> productIPage = productMapper.selectPage(iPage, null);
        productIPage.getRecords();*/

        return ServerResponse.success(a);
    }

    @Override
    public Product selectProductById(Integer productId) {
        Product product = productMapper.selectById(productId);
        return product;
    }

    @Override
    public Long updateStock(int count, Integer id) {
        return productMapper.updateStock(count,id);
    }
}
