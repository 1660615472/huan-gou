package com.cheer.huangou.gateway.filter;

import com.cheer.huangou.common.utils.CookieUtils;
import com.cheer.huangou.gateway.config.FilterProperties;
import com.cheer.huangou.gateway.config.JwtProperties;
import com.huangou.common.utils.JwtUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Slf4j
@Component //用普通控制器注解把过滤器放入spring容器中
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class}) //使使用 @ConfigurationProperties 注解的类生效。
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private FilterProperties filterProperties; //资源文件白名单对象

    @Override //前置过滤
    public String filterType() {
        return "pre";
    }

    @Override //顺序
    public int filterOrder() {
        return 10;
    }

    @Override //开启过滤
    public boolean shouldFilter() {
        //判断请求路径是否在白名单中，如果不在则返回false
        //遍历获取白名单
        List<String> allowPaths = this.filterProperties.getAllowPaths ();

        //初始化运行上下文
        RequestContext context = RequestContext.getCurrentContext ();
        HttpServletRequest request = context.getRequest ();

        //获取当前的请求路径
        String requestURL = request.getRequestURL ().toString ();
        for(String s : allowPaths){
          //判断白名单中是否包含请求路径,包含就拦截，比如：登录时不拦截
            if(StringUtils.contains ( requestURL,s )) {
                return false;
            }
        }

        return true;
    }

    @Override //拦截内容
    public Object run() throws ZuulException {
        //通过zuul网关运行上下文获取request域
        RequestContext context = RequestContext.getCurrentContext ();
        //获取request对象
        HttpServletRequest request = context.getRequest ();
        //获取Cookie中的值
        String token = CookieUtils.getCookieValue ( request, this.jwtProperties.getCookieName () );

        if(StringUtils.isBlank ( token )){
            //如果cookie为空则拦截
            context.setSendZuulResponse ( false );
            //告诉浏览器拦截的状态            UNAUTHORIZED未经授权，无法登录
            context.setResponseStatusCode ( HttpStatus.UNAUTHORIZED.value () );
        }
        //cookie不为空就使用公钥解析
        JwtUtils.parseToken ( this.jwtProperties.getPublicKey (),token );

        return null;
    }
}
