package cn.zeroeden.api;

import cn.zeroeden.api.support.UserSupport;
import cn.zeroeden.domain.JsonResponse;
import cn.zeroeden.domain.UserMoment;
import cn.zeroeden.domain.annotation.ApiLimitedRole;
import cn.zeroeden.domain.annotation.DataLimited;
import cn.zeroeden.domain.constant.AuthRoleConstant;
import cn.zeroeden.service.UserMomnetService;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: Zero
 * @time: 2023/5/23
 * @description: 用户动态
 */

@RestController
public class UserMomentApi {

    @Resource
    private UserMomnetService userMomnetService;

    @Resource
    private UserSupport userSupport;

    /**
     * 用户发布动态
     * @param userMoment 动态信息（具体内容不在这）
     * @return 状态值
     */
    @DataLimited
    @ApiLimitedRole(limitedRoleCodeList = {AuthRoleConstant.ROLE_LV0})
    @PostMapping("/user-moments")
    public JsonResponse<String> addUserMoments(@RequestBody UserMoment userMoment) throws Exception{
        Long userId = userSupport.getCurrentUserId();
        userMoment.setUserId(userId);
        userMomnetService.addUserMoments(userMoment);
        return JsonResponse.success();
    }

    /**
     * 获取当前用户待观看的动态信息
     * @return 动态信息集合
     */
    @GetMapping("/user-subscribed-moments")
    public JsonResponse<List<UserMoment>> getUserSubscribedMoments(){
        Long userId = userSupport.getCurrentUserId();
        List<UserMoment> list = userMomnetService.getUserSubscribedMoments(userId);
        return new JsonResponse<>(list);
    }
}
