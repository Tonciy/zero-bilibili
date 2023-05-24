package cn.zeroeden.domain.annotation;


import java.lang.annotation.*;


/**
 * 数据权限限制注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
//@Component   ？？
public @interface DataLimited {

}
