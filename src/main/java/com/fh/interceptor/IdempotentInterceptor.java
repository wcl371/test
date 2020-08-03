package com.fh.interceptor;

import com.fh.common.Idempotent;
import com.fh.common.Ignore;
import com.fh.common.MyException;
import com.fh.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class IdempotentInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HandlerMethod methodHandle = (HandlerMethod) handler;
        Method method = methodHandle.getMethod();
        //判断该方法是否需要验证幂等性 如果没有@Idempotent 注解 放开
        if(!method.isAnnotationPresent(Idempotent.class)){
            return true;
        }
        //验证幂等性
        String mtoken = request.getHeader("mtoken");
        if (StringUtils.isEmpty(mtoken)){
            throw new MyException("没有mtoken");
        }
        Boolean exists = RedisUtil.exists(mtoken);
        if(!exists){
            throw new MyException("mtoken失效");
        }

        Long del =RedisUtil.del(mtoken);
        if(del==0){
            throw new MyException("重复下单");
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
