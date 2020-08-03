package com.fh.order.service;

import com.fh.cart.model.Cart;
import com.fh.common.ServerResponse;
import com.fh.member.model.User;

import java.util.List;

public interface OrderService {


    ServerResponse buildOrder(List<Cart> cartList, Integer payType, Integer addressId, User user);

}
