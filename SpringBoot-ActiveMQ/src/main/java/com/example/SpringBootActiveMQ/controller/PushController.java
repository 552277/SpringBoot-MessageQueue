package com.example.SpringBootActiveMQ.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.SpringBootActiveMQ.bean.User;
import com.example.SpringBootActiveMQ.service.PushService;
import com.example.SpringBootActiveMQ.utils.ActiveMQUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/push")
public class PushController {

    @Autowired
    private PushService pushService;

    @RequestMapping(value = "text", method = RequestMethod.PATCH)
    public boolean pushMessage(HttpServletRequest request, @RequestParam("user_id") long userId, @RequestParam("message") String message) throws InterruptedException, JMSException, Exception {
        if(StringUtils.isEmpty(userId) || Strings.isEmpty(message)) {
            throw new Exception("参数有误!");
        }
        return ActiveMQUtils.pushTextMessage("id" + userId, message);
    }

    /**
     * 目前传递对象消息通过json实现
     * @param request
     * @return
     */
    @RequestMapping(value = "/object", method = RequestMethod.POST)
    public boolean pushObjectMessage(HttpServletRequest request) {

        long senderId = 666666;
        // 创建对象消息
        User user = new User();
        user.setId(111111l);
        user.setName("Weichang Zhong");
        user.setImageObj("imageUrl");
        user.setMessage("你好，在干嘛呢");
        String jsonStr = JSONObject.toJSONString(user);
        return ActiveMQUtils.pushTextMessage("id" + senderId, jsonStr);
    }
}