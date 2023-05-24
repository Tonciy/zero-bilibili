package cn.zeroeden.api;

import cn.zeroeden.api.support.UserSupport;
import cn.zeroeden.domain.JsonResponse;
import cn.zeroeden.domain.auth.UserAuthorities;
import cn.zeroeden.service.UserAuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: Zero
 * @time: 2023/5/23
 * @description: 用户权限
 */

@RestController
public class UserAuthApi {


    @Resource
    private UserSupport userSupport;


    @Resource
     private  UserAuthService userAuthService;

    /**
     * 获取用户权限--主要是前端的展示权限
     * @return 权限集合集中体
     */
    @GetMapping("/user-authorities")
    public JsonResponse<UserAuthorities> getUserAuthorities(){
        Long userId = userSupport.getCurrentUserId();
        UserAuthorities userAuthorities = userAuthService.getUserAuthorities(userId);
        return new JsonResponse<>(userAuthorities);
    }
}
