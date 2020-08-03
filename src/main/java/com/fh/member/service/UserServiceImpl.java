package com.fh.member.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.common.ServerResponse;
import com.fh.member.mapper.UserMapper;
import com.fh.member.model.User;
import com.fh.util.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public ServerResponse checkMemberName(String name) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",name);
        User user =userMapper.selectOne(queryWrapper);
        if(user==null){
          return ServerResponse.success();
        }
        return ServerResponse.error("用户已存在");
    }

    @Override
    public ServerResponse checkMemberPhone(String phone) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("phone",phone);
        User user =userMapper.selectOne(queryWrapper);
        if(user==null){
            return ServerResponse.success();
        }
        return ServerResponse.error("手机号已存在");
    }

    @Override
    public ServerResponse register(User user) {
        String redisCode = RedisUtil.get(user.getPhone());
        if(redisCode==null){
             return ServerResponse.error("验证码失效");
        }
        if(!redisCode.equals(user.getCode())){
            return ServerResponse.error("验证码错误");
        }
        userMapper.insert(user);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse login(User user) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",user.getName());
        queryWrapper.or();
        queryWrapper.eq("phone",user.getName());
        User userDB = userMapper.selectOne(queryWrapper);
        if(userDB==null){
              return ServerResponse.error("用户或手机号不存在");
        }

        if(!userDB.getPassword().equals(userDB.getPassword())){
               return ServerResponse.error("密码错误");
        }
        //账号密码正确，生成token返回前台
        String token = null;
        try {
            String jsonString = JSONObject.toJSONString(userDB);
            //编译格式
            String encodeString = URLEncoder.encode(jsonString, "utf-8");
            token = JwtUtil.sign(encodeString);
            RedisUtil.setex(SystemConstant.TOKEN_KEY+token,token,SystemConstant.TOKEN_EXPIRE_TIME);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ServerResponse.success(token);

    }

    @Override
    public ServerResponse queryUserList() {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        List<User> userList = userMapper.selectList(queryWrapper);
        return ServerResponse.success(userList);
    }

}
