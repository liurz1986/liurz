package cn.com.usercenter.monitor;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;

/**
 * 监控入口（日志监控）---监控controller
 * 使用面向切面的实现下面两个目的：
 * 1.实现每次登陆的时身份验证
 * 2.监控每次请求的路径参数和地址，返回的参数
 * 备注：自动代理配置必须在mvc的xml配置
 * @author Administrator
 *
 */
@Aspect
@Component
public class AccessMonitor {
	private final static Logger log=LoggerFactory.getLogger(AccessMonitor.class);
	//切点    监听controller
    @Pointcut("execution(* cn.com.usercenter.controller.*.*(..))")
	public void  login(){};
	
	 //自定义前置通知
	@Before("login()")
    public void before(JoinPoint point){
	  ServletRequestAttributes requestAttributes=(ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
	  HttpServletRequest request=requestAttributes.getRequest();
	  String url=request.getRequestURI();//访问的url
	  String Ip=request.getRemoteAddr();//访问的ip地址
   	  Object[] obj=point.getArgs();//执行 方法时传递的参数
   	  Object object=point.getTarget();
   	  String className=object.getClass().getSimpleName();//类名
   	  String method=point.getSignature().getName();//方法名
   	  StringBuffer buffer=new StringBuffer();
   	  for(Object objec:obj){
   		  if(!StringUtils.isEmpty(objec)){
   			  buffer.append(objec.toString()+"-");
      		  log.info("访问时带的参数："+objec.toString());
   		  }
   		 
   	  }
   	  log.info("访问的类名："+className);
   	  log.info("访问的方法名："+method);
   	  
   	  String result="请求的Ip"+Ip+"/"+url;
   	  log.info(result);
    } 
	//自定义后置通知返回结果
	@AfterReturning(returning="object",pointcut="login()")
    public void after(Object object){
  	  log.info("响应返回结果："+JSON.toJSONString(object).toString());
  	
    }
}
