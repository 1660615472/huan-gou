package com.huangou.sms.listener;
import com.huangou.sms.config.SmsProperties;
import com.huangou.sms.utils.SmsUtil;
import org.apache.commons.lang3.StringUtils;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;


import java.util.Map;


/**
 * @author zhangjie
 * @title:
 * @data
 */

@Controller //放入spring容器
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @Autowired
    private SmsProperties smsProperties; //配置属性类

    @RabbitListener(bindings = @QueueBinding (
      value = @Queue(value = "huangou.sms.queue",durable = "true"),
            exchange = @Exchange(value = "huangou.sms.exchange",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"verifycode.sms"}
    )) //监听用户服务是否发送验证码过来
    public void sendSms(Map<String,String> msg)throws ClientException{
        if(CollectionUtils.isEmpty ( msg )){
            //为空就放弃处理
            return;
        }

        String phone = msg.get ( "phone" );//手机号
        String code = msg.get ( "code" );//验证码

        if(StringUtils.isBlank(phone) && StringUtils.isNoneBlank ( code )){
            //发送消息
            this.smsUtil.sendSms ( phone,code,this.smsProperties.getSignName (),this.smsProperties.getVerifyCodeTemplate () );
        }
    }


}
