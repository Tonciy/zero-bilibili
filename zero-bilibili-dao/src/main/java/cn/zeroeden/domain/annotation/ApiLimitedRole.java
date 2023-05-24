package cn.zeroeden.domain.annotation;


import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;


/**
 * 接口访问角色限制注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
//@Component
public @interface ApiLimitedRole {

    String[] limitedRoleCodeList() default {};
}
