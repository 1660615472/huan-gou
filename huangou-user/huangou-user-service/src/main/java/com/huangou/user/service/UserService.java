package com.huangou.user.service;

import com.cheer.huangou.common.utils.NumberUtils;
import com.huangou.user.mapper.UserMapper;
import com.huangou.user.pojo.User;
import com.huangou.user.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangjie
 * @title:
 * @data
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;

    //设置前缀区分不同的短信验证码(登录验证，订单验证等)
    private static final String key_prefix ="user:verify:code:";

    /**
     * 检查用户登录
     * @param data 用户登录信息
     * @param type 要效验的数据类型 1为用户名 2为手机
     * @return
     */
    public Boolean checkUser(String data,Integer type) {
        User record = new User ();
        if(type == 1){
            record.setUsername ( data );
        }else if(type == 2) {
            record.setPhone ( data );
        }else {
            return null; //参数不合法
        }
        //数据库找到就不能注册了
     return  this.userMapper.selectCount ( record ) ==0;
    }

    /**
     * 发送短信验证码方法
     * @param phone
     */
    public void sendVerifyCode(String phone) {
    /*    if(StringUtils.isNoneBlank ( phone )){
            //号码为空放弃执行
            return;
        }*/
        //根据电话号码生成6位数验证码
        String code = NumberUtils.generateCode ( 6 );

        //发送消息到rubbitmq   消费者端是用map来接收的
        Map<String,String> map = new HashMap<> (  ); //短信服务的交换机接收的就是map所以传也要传map过去
        map.put ( "phone",phone );
        map.put("code",code);
        //把验证码保存到redis中
        stringRedisTemplate.opsForValue ().set ( key_prefix+phone,code,5, TimeUnit.MINUTES );
        amqpTemplate.convertAndSend ( "huangou.sms.exchange","verifycode.sms",map );


    }

    /**
     * 判断验证码是否正确
     * @param user
     * @param code
     * @return
     */
    public Boolean register(User user, String code)throws Exception {
        String key =key_prefix+ user.getPhone ();
        //效验验证码，从redis中取出验证码对比
        String codeCache = this.stringRedisTemplate.opsForValue ().get ( key );
        if(!codeCache.equals ( code )){
            //验证码不正确返回
            return false;
        }
        user.setId ( null );
        user.setCreated ( new Date (  ) );
        //生成盐
        String salt = CodecUtils.generateSalt ();
        user.setSalt ( salt );

        //生成加密的密码+盐
       String md5Password = CodecUtils.md5Hex ( user.getPassword (),user.getSalt () );
        user.setPassword ( md5Password );

        //将创建好的用户信息保存到数据库
        int insert = this.userMapper.insert ( user );
        if(insert!=1){
            //没创建成功抛异常
            throw new Exception ( "创建用户失败" );
        }

        //把验证码从redis中删除
        this.stringRedisTemplate.delete ( key );

        return true;
    }

    //查询数据库是否存在这个用户
    public User queryUser(String username, String password) {
        //new一个user对象，根据User查询
        User user = new User ();
        user.setUsername ( username );
        User user1 = this.userMapper.selectOne ( user );
        if(user1 == null){
            return null;
        }

        //效验密码  (如果输入的密码经过加密后加上盐不equals数据库中的密码)
        if(!user1.getPassword ().equals ( CodecUtils.md5Hex ( password,user1.getSalt () ) )){
            return null;
        }

        //查到数据库有该账号信息就ok
        return user;
    }
}
