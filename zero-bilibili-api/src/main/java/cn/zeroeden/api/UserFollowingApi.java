package cn.zeroeden.api;

import cn.zeroeden.api.support.UserSupport;
import cn.zeroeden.domain.FollowingGroup;
import cn.zeroeden.domain.JsonResponse;
import cn.zeroeden.domain.UserFollowing;
import cn.zeroeden.service.FollowingGroupService;
import cn.zeroeden.service.UserFollowingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: Zero
 * @time: 2023/5/18
 * @description: 关注用户 和 关注分组
 */

@RestController
public class UserFollowingApi {

    @Resource
    private UserFollowingService userFollowingService;

    @Resource
    private UserSupport userSupport;

    @Resource
    private FollowingGroupService followGroupService;


    /**
     * 用户新增关注其他用户
     * @param userFollowing 关注
     * @return 状态值
     */
    @PostMapping("/user-followings")
    public JsonResponse<String> adduserFollowings(@RequestBody UserFollowing userFollowing){
        Long userId = userSupport.getCurrentUserId();
        userFollowingService.addUserFollowings(userFollowing);
        return JsonResponse.success();
    }

    /**
     * 获取当前用户的各个关注列表用户信息
     * @return 信息
     */
    @GetMapping("/user-followings")
    public JsonResponse<List<FollowingGroup>> getUserFollowings(){
        Long userId = userSupport.getCurrentUserId();
        List<FollowingGroup> result = userFollowingService.getUserFollowings(userId);
        return new JsonResponse<>(result);
    }

    /**
     * 获取粉丝信息
     * @return 粉丝信息
     */
    @GetMapping("/user-fans")
    public JsonResponse<List<UserFollowing>> getUserinfo(){
        Long userId = userSupport.getCurrentUserId();
        List<UserFollowing> result = userFollowingService.getUserFans(userId);
        return new JsonResponse<>(result);
    }


    /**
     * 新增用户关注分组
     * @param followingGroup 用户关注分组
     * @return 关注分组id
     */
    @PostMapping("/user-following-groups")
    public JsonResponse<Long> addUserFollowingGroups(@RequestBody FollowingGroup followingGroup){
        Long userId = userSupport.getCurrentUserId();
        followingGroup.setUserId(userId);
        Long groupId = userFollowingService.addUserFollowingGroups(followingGroup);
        return new JsonResponse<>(groupId);
    }


    /**
     * 获取当前用户的关注分组
     * @return 关注分组列表
     */
    @GetMapping("/user-following-groups")
    public JsonResponse<List<FollowingGroup>> getUserFollowingGroups(){
        Long userId = userSupport.getCurrentUserId();
        List<FollowingGroup> list = followGroupService.getUserFollowingGroups(userId);
        return new JsonResponse<>(list);
    }
}
