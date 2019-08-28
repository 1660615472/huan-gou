package com.huangou.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oracle.webservices.internal.api.databinding.DatabindingMode;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @author zhangjie
 * @title:用户信息实体类
 * @data
 */
@Data
@Table(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min=4, max=30,message = "用户名必须在4-30位之间") //约束字符串长度
    private String username;// 用户名

    @Length(min=4, max=30,message = "用户名必须在4-30位之间")
    @JsonIgnore  //JsonIgnore作用： 对象序列化为json字符串时，忽略该属性
    private String password;// 密码

    @Pattern(regexp = "^1[356789]\\d{9}$",message = "手机号格式不正确")
    private String phone;// 电话

    private Date created;// 创建时间

    @JsonIgnore //当查询这个user类时候jsonIgnore注释的字段不会被查出来
    private String salt;// 密码的盐值
}