package com.fh.order.service;

import com.alibaba.fastjson.JSONObject;
import com.fh.cart.model.Cart;
import com.fh.common.ServerResponse;
import com.fh.member.model.User;
import com.fh.order.mapper.OrderInfoMapper;
import com.fh.order.mapper.OrderMapper;
import com.fh.order.model.Order;
import com.fh.order.model.OrderInfo;
import com.fh.product.model.Product;
import com.fh.product.service.ProductService;
import com.fh.util.BigDecimalUtil;
import com.fh.util.IdUtil;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private ProductService productService;

    //订单 order
    //订单详情 orderInfo

    @Override
    @Transactional
    public ServerResponse buildOrder(List<Cart> cartList, Integer payType, Integer addressId, User user) {

        String orderId = IdUtil.createId();

        List<OrderInfo> orderInfoList = new ArrayList<>();

        //商品总价格
        BigDecimal totalPrice = new BigDecimal("0.00");
        //库存不足集合
        List<String> stockNotFull =new LinkedList<>();

        for (Cart cart : cartList) {
            Product product = productService.selectProductById(cart.getProductId());
             //商品中的库存小于购物车的数量
            if(product.getStock()<cart.getCount()){
                //库存不足
                stockNotFull.add(cart.getName());
            }else {

                //减库存 判断库存是否充足
                Long res = productService.updateStock(cart.getCount(), product.getId());
                if (res == 1) {
                    //库存充足 生成订单详情
                    OrderInfo orderInfo = buildOrderInfo(orderId, cart);
                    orderInfoList.add(orderInfo);
                    BigDecimal subTotal = BigDecimalUtil.mul(cart.getPrice().toString(), cart.getCount() + "");
                    totalPrice = BigDecimalUtil.add(totalPrice, subTotal);

                } else {
                    //库存不足
                    stockNotFull.add(cart.getName());
                }
            }
        }
            //生成订单 先判断是否有库存不足的商品
        if(orderInfoList !=null && orderInfoList.size()==cartList.size() ){
            //库存都足  保存订单详细
            for (OrderInfo orderInfo : orderInfoList) {
                orderInfoMapper.insert(orderInfo);
                //更新购物车
                updateRedisCart(user, orderInfo);
            }
              //  生成订单
            buildOrder(payType, addressId, user, orderId, totalPrice);
            return ServerResponse.success(orderId);
        }else {
            return ServerResponse.error(stockNotFull);
        }

    }



    private void buildOrder(Integer payType, Integer addressId, User user, String orderId, BigDecimal totalPrice) {
        Order order = new Order();
        order.setCreateDate(new Date());
        order.setPayType(payType);
        order.setAddressId(addressId);
        order.setId(orderId);
        order.setUserId(user.getId());
        order.setTotalPrice(totalPrice);
        order.setStatus(SystemConstant.ORDER_STATUS_WAIT);
        orderMapper.insert(order);
    }

    private OrderInfo buildOrderInfo(String orderId, Cart cart) {
        OrderInfo orderInfo =new OrderInfo();
        orderInfo.setName(cart.getName());
        orderInfo.setFilePath(cart.getFilePath());
        orderInfo.setPrice(cart.getPrice());
        orderInfo.setOrderId(orderId);
        orderInfo.setProductId(cart.getProductId());
        orderInfo.setCount(cart.getCount());
        return orderInfo;
    }

    private void updateRedisCart(User user, OrderInfo orderInfo) {
        String cartJson = RedisUtil.hget(SystemConstant.CART_KEY + user.getId(), orderInfo.getProductId().toString());
        if(StringUtils.isNotEmpty(cartJson)){
            Cart cart1 = JSONObject.parseObject(cartJson, Cart.class);
            if(cart1.getCount()<=orderInfo.getCount()){
                //删除购物车中该商品
                RedisUtil.hdel(SystemConstant.CART_KEY + user.getId(), orderInfo.getProductId().toString());

            }else{
                //更新购物车
                cart1.setCount(cart1.getCount()-orderInfo.getCount());
                String s = JSONObject.toJSONString(cart1);
                RedisUtil.hset(SystemConstant.CART_KEY + user.getId(), orderInfo.getProductId().toString(),s);
            }

        }
    }
}
