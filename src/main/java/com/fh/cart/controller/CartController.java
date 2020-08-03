package com.fh.cart.controller;

import com.alibaba.fastjson.JSONObject;
import com.fh.cart.model.Cart;
import com.fh.cart.service.CartService;
import com.fh.common.ServerEnum;
import com.fh.common.ServerResponse;
import com.fh.common.UserAnnotation;
import com.fh.member.model.User;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;


@RestController
@RequestMapping("cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping("buy")
    public ServerResponse buy(Integer productId, Integer count, HttpServletRequest request){
        return cartService.buy(productId,count,request);
    }

    @RequestMapping("queryCartProductCount")
    public ServerResponse queryCartProductCount(@UserAnnotation User user){
        List<String> stringList = RedisUtil.hget(SystemConstant.CART_KEY + user.getId());
        long totalCount=0;
        if(stringList!=null && stringList.size()>0){
            for (String str : stringList) {
                Cart cart = JSONObject.parseObject(str, Cart.class);
                   totalCount+=cart.getCount();
            }
        }else {
            return ServerResponse.success(0);
        }
        return ServerResponse.success(totalCount);
    }



    @RequestMapping("queryList")
    public ServerResponse queryList(@UserAnnotation User user){
        List<String> stringList = RedisUtil.hget(SystemConstant.CART_KEY + user.getId());
        List<Cart> cartList=new LinkedList<>();
        if(stringList!=null && stringList.size()>0){
            for (String str : stringList) {
                Cart cart = JSONObject.parseObject(str, Cart.class);
                cartList.add(cart);
            }
        }else {
            return ServerResponse.error(ServerEnum.CART_IS_NULL.getMsg());
        }
        return ServerResponse.success(cartList);
    }

    @RequestMapping("deleteProduct/{productId}")
    public ServerResponse deleteProduct(@UserAnnotation User user,@PathVariable("productId") Integer productId){
        RedisUtil.hdel(SystemConstant.CART_KEY + user.getId(),productId.toString());

        return ServerResponse.success();
    }

}
