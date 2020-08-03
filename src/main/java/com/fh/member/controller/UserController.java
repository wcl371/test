package com.fh.member.controller;

import com.fh.common.Ignore;
import com.fh.common.ServerResponse;
import com.fh.member.model.User;
import com.fh.member.service.UserService;
import com.fh.util.RedisUtil;
import com.fh.util.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("checkMemberName")
    @Ignore
    public ServerResponse checkMemberName(String name){
        return userService.checkMemberName(name);
    }

    @RequestMapping("checkMemberPhone")
    @Ignore
    public ServerResponse checkMemberPhone(String phone){
        return userService.checkMemberPhone(phone);
    }


    @RequestMapping("register")
    @Ignore
    public ServerResponse register(User user){
        return userService.register(user);
    }



    @RequestMapping("login")
    @Ignore
    public ServerResponse login(User user){
        return userService.login(user);
    }

    @RequestMapping("checkLogin")
    @Ignore
    public ServerResponse checkLogin(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
        if(user==null){
            return ServerResponse.error();
        }
        return ServerResponse.success();
    }

    @RequestMapping("out")
    @Ignore
    public ServerResponse out(HttpServletRequest request){
        //让token失效
        String token = (String) request.getSession().getAttribute(SystemConstant.TOKEN_KEY);
        RedisUtil.del(SystemConstant.TOKEN_KEY+token);

        request.getSession().removeAttribute(SystemConstant.TOKEN_KEY);
        //清除session中用户的信息
        request.getSession().removeAttribute(SystemConstant.SESSION_KEY);
        return ServerResponse.success();
    }



    @RequestMapping("queryUserList")
    public ServerResponse queryUserList(){
        return userService.queryUserList();
    }




}
