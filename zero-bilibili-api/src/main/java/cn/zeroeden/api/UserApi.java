package cn.zeroeden.api;

import cn.zeroeden.api.support.UserSupport;
import cn.zeroeden.domain.JsonResponse;
import cn.zeroeden.domain.User;
import cn.zeroeden.service.UserService;
import cn.zeroeden.service.util.RSAUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: Zero
 * @time: 2023/5/17
 * @description:
 */

@RestController
public class UserApi {

    @Resource
    private UserService userService;


    @Resource
    private UserSupport userSupport;
    /**
     * 获取RSA公钥
     * @return RSA公钥
     */
    @GetMapping("/rsa-pks")
    public JsonResponse<String> getRsaPublicKey(){
        return new JsonResponse<>(RSAUtil.getPublicKeyStr());
    }


    /**
     * 用户注册
     * @param user 用户账号信息
     * @return 状态值
     */
    @PostMapping("/users")
    public JsonResponse<String> addUser(@RequestBody User user){
        userService.addUser(user);
        return JsonResponse.success();
    }

    /**
     * 用户登录
     * @param user 用户账户信息
     * @return token令牌
     */
    @PostMapping("/users-tokens")
    public JsonResponse<String> login(@RequestBody User user) throws Exception {
        String token = userService.login(user);
        return JsonResponse.success(token);
    }

    @GetMapping("/users")
    public JsonResponse<User> getUserInfo(){
        Long userId = userSupport.getCurrentUserId();
        User user  = userService.getUserInfo(userId);
        return new JsonResponse<>(user);
    }
}
