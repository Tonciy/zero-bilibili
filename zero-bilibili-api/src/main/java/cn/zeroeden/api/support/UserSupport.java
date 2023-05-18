package cn.zeroeden.api.support;

import cn.zeroeden.domain.exception.ConditionException;
import cn.zeroeden.service.util.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author: Zero
 * @time: 2023/5/17
 * @description: 用户的一些相关组件
 */

@Component
public class UserSupport {

    public Long getCurrentUserId(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = requestAttributes.getRequest().getHeader("token");
        Long userId = TokenUtil.verifyToken(token);
        if(userId <  0){
            throw new ConditionException("非法用户！");
        }
        return userId;
    }
}
