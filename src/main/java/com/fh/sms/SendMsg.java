package com.fh.sms;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.fh.common.ServerResponse;
import com.fh.util.MessageUtil;
import com.fh.util.RedisUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sms")
public class SendMsg {

    @RequestMapping("sendMsg")
    public ServerResponse sendMsg(String phone){
        String code = MessageUtil.getNewcode();
        try {
            SendSmsResponse sendSmsResponse = MessageUtil.sendSms(phone, code);
            if(sendSmsResponse !=null && "OK".equals(sendSmsResponse.getCode())){
              //把code 放到redis
                RedisUtil.set(phone,code);
                return ServerResponse.success();
            }
        } catch (ClientException e) {
            e.printStackTrace();
            return ServerResponse.error(e.getErrMsg());
        }
        return null;
    }

}
