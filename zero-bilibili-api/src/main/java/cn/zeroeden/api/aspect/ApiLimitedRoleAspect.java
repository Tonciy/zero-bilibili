package cn.zeroeden.api.aspect;

import cn.zeroeden.api.support.UserSupport;
import cn.zeroeden.domain.annotation.ApiLimitedRole;
import cn.zeroeden.domain.auth.UserRole;
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
 * @description: 接口访问的角色限制切面
 */

@Aspect
@Component
@Order(1)
public class ApiLimitedRoleAspect {

    @Resource
    private UserSupport userSupport;

    @Resource
    private UserRoleService userRoleService;

    @Pointcut("@annotation(cn.zeroeden.domain.annotation.ApiLimitedRole)")
    public void check(){

    }

    @Before("check() && @annotation(apiLimitedRole)")
    public void doBefore(JoinPoint joinPoint, ApiLimitedRole apiLimitedRole){
        Long userId = userSupport.getCurrentUserId();
        List<UserRole> roleList = userRoleService.getUserRolesByUserId(userId);
        String[] limitedRoleCodeList = apiLimitedRole.limitedRoleCodeList();
        Set<String> limitedRoleCodeSet = Arrays.stream(limitedRoleCodeList).collect(Collectors.toSet());
        Set<String> roleCodeSet = roleList.stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
        roleCodeSet.retainAll(limitedRoleCodeSet);// 取交集
        if(roleCodeSet.size() > 0){
            throw new ConditionException("权限不足！");
        }
    }


}
