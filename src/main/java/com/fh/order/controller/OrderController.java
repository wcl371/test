package com.fh.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.fh.cart.model.Cart;
import com.fh.common.Idempotent;
import com.fh.common.ServerResponse;
import com.fh.common.UserAnnotation;
import com.fh.member.model.User;
import com.fh.order.service.OrderService;
import com.fh.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("buildOrder")
    @Idempotent
    public ServerResponse buildOrder(String listStr, Integer payType, Integer addressId, @UserAnnotation User user){
        List<Cart> cartList =new LinkedList<>();
        if(StringUtils.isNoneEmpty(listStr)){
             cartList = JSONObject.parseArray(listStr, Cart.class);
        }else {
            return ServerResponse.error("请选择商品");
        }
        return orderService.buildOrder(cartList,payType,addressId,user);
    }


    /*@RequestMapping("queryStock")
    public ServerResponse queryStock(String listStr){
        List<Cart> cartList =new LinkedList<>();
        if(StringUtils.isNoneEmpty(listStr)){
            cartList = JSONObject.parseArray(listStr, Cart.class);
        }else {
            return ServerResponse.error("请选择商品");
        }
        return orderService.queryStock(cartList);
    }*/

    @RequestMapping("getToken")
    public ServerResponse getToken(){
        String mtoken = UUID.randomUUID().toString();
        RedisUtil.set(mtoken,mtoken);
        return ServerResponse.success(mtoken);
    }




}
