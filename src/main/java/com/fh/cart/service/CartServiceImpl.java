package com.fh.cart.service;

import com.alibaba.fastjson.JSONObject;
import com.fh.cart.model.Cart;
import com.fh.common.ServerEnum;
import com.fh.common.ServerResponse;
import com.fh.member.model.User;
import com.fh.product.model.Product;
import com.fh.product.service.ProductService;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class CartServiceImpl implements CartService {

    @Resource
    private ProductService productService1;

    @Override
    public ServerResponse buy(Integer productId,Integer count, HttpServletRequest request) {
         //验证商品是否存在

        Product product=productService1.selectProductById(productId);

        if(product==null){
           return ServerResponse.error(ServerEnum.PRODUCT_NOT_EXIST);
        }
         //验证商品是否上架
        if(product.getStatus()==0){
            return ServerResponse.error(ServerEnum.PRODUCT_IS_DOWN);
        }
         //验证购物车是否存在改商品
        User user = (User) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
        Boolean exists = RedisUtil.exists(SystemConstant.CART_KEY, productId.toString());
        if(!exists) {
            Cart cart = new Cart();
            cart.setCount(count);
            cart.setProductId(productId);
            cart.setName(product.getName());
            cart.setPrice(product.getPrice());
            cart.setFilePath(product.getFilePath());
            String jsonString = JSONObject.toJSONString(cart);
            RedisUtil.hset(SystemConstant.CART_KEY+user.getId(),productId.toString(),jsonString);

        }else {
            //如果商品存在则修改数量
            String productJson = RedisUtil.hget(SystemConstant.CART_KEY + user.getId(), productId.toString());
            //把JSON转化为对象
            Cart cart = JSONObject.parseObject(productJson, Cart.class);
            cart.setCount(cart.getCount()+count);
            //修改完成之后存入Json中
            String jsonString = JSONObject.toJSONString(cart);
            RedisUtil.hset(SystemConstant.CART_KEY+user.getId(),productId.toString(),jsonString);


        }
        return ServerResponse.success();
    }

}
