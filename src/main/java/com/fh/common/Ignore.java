package com.fh.common;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)//用于描述注解使用的范围 用在方法上面
@Retention(RetentionPolicy.RUNTIME)//定义了该注解被保留的时间的长短
public @interface Ignore {

    //不能继承其他类其它接口
}
