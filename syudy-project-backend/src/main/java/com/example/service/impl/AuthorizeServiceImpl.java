package com.example.service.impl;

import com.example.entity.Account;
import com.example.mapper.UserMapper;
import com.example.service.AuthorizeService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AuthorizeServiceImpl implements AuthorizeService {


    @Resource
    UserMapper mapper;

    @Resource
    MailSender mailSender;

    @Resource
    StringRedisTemplate template;

    //从数据库取值
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null){
            throw new UsernameNotFoundException("用户名不能为空");
        }
        Account account = mapper.findAccountByNameOrEmail(username);
        if (account == null) {
            throw new UsernameNotFoundException("用户名不能为空");
        }
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles("user")
                .build();
    }

        /**
         * 1. 生成对应的验证码
         * 2. 把邮箱对应的验证码直接放在Redis里面（过期时间为3分钟，如果此时重新要求发送邮件，
         * 那么，只要剩余时间低于2分钟，就可以重新发送一次，重复流程）
         * 3. 发送验证码到指定邮箱
         * 4. 如果发送失败，把Redis里面刚刚插入的删除
         * 5. 用户在注册时，再从Redis里面取出对应键值对，然后看验证码是否一致
         */
    @Override
    public boolean sendValidateEmail(String email, String sessionId) {
        String key = "email:" + sessionId + ":" +email;
        if (Boolean.TRUE.equals(template.hasKey(key))){
            Long expire = Optional.ofNullable(template.getExpire(key, TimeUnit.SECONDS)).orElse(0L);
            if (expire > 120) {
                return false;
            }
        }
        Random random = new Random();
        int code = random.nextInt(899999) + 100000;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("daiwei4048@163.com");
        message.setTo(email);
        message.setSubject("【前后端分离项目】验证邮件");
        message.setText("您的验证码是: " +code +" 3分钟内有效。请勿向他人泄露。如非本人操作，可忽略本消息。");
        try {
            mailSender.send(message);
            template.opsForValue().set(key, String.valueOf(code),3, TimeUnit.MINUTES);
            return true;
        }catch (MailException e){
            e.printStackTrace();
            return false;
        }
    }
}
