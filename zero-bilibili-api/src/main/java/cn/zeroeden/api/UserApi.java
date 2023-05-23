package cn.zeroeden.api;

import cn.zeroeden.api.support.UserSupport;
import cn.zeroeden.domain.JsonResponse;
import cn.zeroeden.domain.PageResult;
import cn.zeroeden.domain.User;
import cn.zeroeden.domain.UserInfo;
import cn.zeroeden.service.UserFollowingService;
import cn.zeroeden.service.UserService;
import cn.zeroeden.service.util.RSAUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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

    @Resource
    private UserFollowingService userFollowingService;
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

    /**
     * 获取当前用户信息
     * @return 用户信息
     */
    @GetMapping("/users")
    public JsonResponse<User> getUserInfo(){
        // 当前用户id从token拿
        Long userId = userSupport.getCurrentUserId();
        User user  = userService.getUserInfo(userId);
        return new JsonResponse<>(user);
    }

    /**
     * 更新用户账号信息
     * @param user 用户账号信息
     * @return 状态值
     */
    @PutMapping("/users")
    public JsonResponse<String> updateUsersById(@RequestBody User user) throws Exception{
        Long userId = userSupport.getCurrentUserId();
        user.setId(userId);
        userService.updateUsersById(user);
        return JsonResponse.success();
    }



    /**
     * 更新用户个人信息
     * @param userInfo 用户个人信息
     * @return 状态值
     */
    @PutMapping("/user-infos")
    public JsonResponse<String> updateUserInfos(@RequestBody UserInfo userInfo){
        Long userId = userSupport.getCurrentUserId();
        userInfo.setUserId(userId);
        userService.updateUserInfosByUserId(userInfo);
        return JsonResponse.success();
    }

    /**
     * 分页获取用户信息
     * @param no 当前页
     * @param size 一页展示数量
     * @param nick 昵称-可选
     * @return 分页数据
     */
    @GetMapping("/user-infos")
    public JsonResponse<PageResult<UserInfo>> pageListUserInfos(@RequestParam(defaultValue = "1") Integer no,
                                                                @RequestParam(defaultValue = "10") Integer size,
                                                                String nick){
        Long userId = userSupport.getCurrentUserId();
        // 封装参数
        JSONObject params = new JSONObject();
        params.put("no", no);
        params.put("size", size);
        params.put("nick", nick);
        params.put("userId", userId);
        PageResult<UserInfo> result = userService.pageListUserInfos(params);
        if(result.getList().size() > 0){
            // 如果搜到的用户已经是当前用户关注了，设置其关注状态值方便前端显示
            List<UserInfo> checkedUserInfoList = userFollowingService.checkFollowingStatus(result.getList(), userId);
            result.setList(checkedUserInfoList);
        }
        return new JsonResponse<>(result);
    }
}
