package cn.zeroeden.api.aspect;

import cn.zeroeden.api.support.UserSupport;
import cn.zeroeden.domain.UserMoment;
import cn.zeroeden.domain.annotation.ApiLimitedRole;
import cn.zeroeden.domain.auth.UserRole;
import cn.zeroeden.domain.constant.AuthRoleConstant;
import cn.zeroeden.domain.exception.ConditionException;
import cn.zeroeden.service.UserRoleService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: Zero
 * @time: 2023/5/24
 * @description: 数据权限控制
 */

@Aspect
@Component
@Order(1)
public class DataLimitedAspect {

    @Resource
    private UserSupport userSupport;

    @Resource
    private UserRoleService userRoleService;

    @Pointcut("@annotation(cn.zeroeden.domain.annotation.DataLimited)")
    public void check(){

    }

    @Before("check()")
    public void doBefore(JoinPoint joinPoint){
        Long userId = userSupport.getCurrentUserId();
        List<UserRole> roleList = userRoleService.getUserRolesByUserId(userId);
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if(arg instanceof UserMoment){
                UserMoment userMoment = (UserMoment) arg;
                String type = userMoment.getType();
                if(roleList.contains(AuthRoleConstant.ROLE_LV0) && !"0".equals(type)){
                    throw new ConditionException("参数异常！");
                }
            }
        }
    }


}
