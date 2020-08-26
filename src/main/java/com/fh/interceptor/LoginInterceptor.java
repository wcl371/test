package com.fh.interceptor;


import com.alibaba.fastjson.JSONObject;
import com.fh.common.Ignore;
import com.fh.member.model.User;
import com.fh.util.JwtUtil;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.net.URLDecoder;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        //处理客户端传过来的自定义信息
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,"x-auth,mtoken,content-type");
        //处理客户端发过来 put,delete
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,"PUT.POST,DELETE,GET");

        HandlerMethod methodHandle = (HandlerMethod) handler;
        Method method = methodHandle.getMethod();
         //判断该方法是否需要拦截 如果有@Ignore 注解 不用拦截
        if(method.isAnnotationPresent(Ignore.class)){
            return true;
        }
        //获取请求头里面的token
        String token = request.getHeader("x-auth");
        //如果没有token返回登陆页面
        if(StringUtils.isEmpty(token)){
           throw new LoginException();
        }
        //验证token是否失效
        Boolean exists = RedisUtil.exists(SystemConstant.TOKEN_KEY+token);
         if(!exists){
             //token失效
             throw new LoginException();
         }

        //验证token是否正确
        boolean use = JwtUtil.verify(token);
        if(use) {
            //获取token的信息
            String userString = JwtUtil.getUser(token);
            //编译格式
            String jsonUser = URLDecoder.decode(userString, "utf-8");
            //将json形式转化为对象
            User user = JSONObject.parseObject(jsonUser, User.class);
            //把对象放入session里面
            request.getSession().setAttribute(SystemConstant.SESSION_KEY,user);
            request.getSession().setAttribute(SystemConstant.TOKEN_KEY,token);
            System.out.println(user.toString());
         }else {
            throw new LoginException();
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
