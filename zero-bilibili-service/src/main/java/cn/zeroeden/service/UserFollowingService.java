package cn.zeroeden.service;

import cn.zeroeden.dao.UserFollowingDao;
import cn.zeroeden.domain.FollowingGroup;
import cn.zeroeden.domain.User;
import cn.zeroeden.domain.UserFollowing;
import cn.zeroeden.domain.UserInfo;
import cn.zeroeden.domain.constant.UserConstant;
import cn.zeroeden.domain.exception.ConditionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: Zero
 * @time: 2023/5/18
 * @description:
 */

@Service
public class UserFollowingService {
    @Resource
    private UserFollowingDao userFollowingDao;

    @Resource
    private FollowingGroupService followGroupService;

    @Resource
    private UserService userService;

    /**
     * 用户关注其他用户
     * @param userFollowing
     */
    @Transactional
    public void addUserFollowings(UserFollowing userFollowing){
        // 看看分组类型
        Long groupId = userFollowing.getGroupId();
        if(groupId == null){
            // 默认分组
            FollowingGroup followingGroup = followGroupService.getByType(UserConstant.USER_FOLLOWING_GROUP_TYPE_DEFALUT);
            userFollowing.setGroupId(followingGroup.getId());
        }else{
            // 校验下这个分组存不存在
            FollowingGroup followingGroup = followGroupService.getById(groupId);
            if(followingGroup == null){
                throw new ConditionException("关注分组不存在!");
            }
        }
        // 校验关注的用户存不存在
        Long followingId = userFollowing.getFollowingId();
        User user = userService.getUserById(followingId);
        if(user == null){
            throw new ConditionException("关注的用户不存在!");
        }
        // 删掉原有的
        userFollowingDao.deleteUserFollowingByUserIdAndFollowingId(userFollowing.getId(), userFollowing.getFollowingId());
        userFollowing.setCreateTime(new Date());
        userFollowingDao.addUserFollowing(userFollowing);
    }

    /**
     * 获取用户关注分组（含各个分组内的关注用户信息）
     * @param userId 用户id
     * @return
     */
    public List<FollowingGroup> getUserFollowings(Long userId){
        // 1. 先查询出关注的所有用户信息
        List<UserFollowing> list = userFollowingDao.getUserFollowing(userId);
        // 2. 获取关注用户的所有id
        Set<Long> followingIds = list.stream().map(UserFollowing::getFollowingId).collect(Collectors.toSet());
        List<UserInfo> userInfoList = new ArrayList<>();
        if(followingIds != null && followingIds.size() > 0){
            // 3. 查询关注用户的基本信息
            userInfoList = userService.getUserByUserIds(followingIds);
        }
        // 4. 把用户基本信息装在到对应的UserFollowing中去
        for (UserFollowing userFollowing : list) {
            for (UserInfo userInfo : userInfoList) {
                if(userFollowing.getFollowingId().equals(userInfo.getUserId()) ){
                    userFollowing.setUserInfo(userInfo);
                }
            }
        }
        // 5. 查询该用户的所有关注分组信息
        List<FollowingGroup> groupLsit = followGroupService.getByUserId(userId);
        // 再添加个全部关注的分组
        FollowingGroup allGroup = new FollowingGroup();
        allGroup.setName(UserConstant.USER_FOLLOWING_GROUP_ALL_NAME);
        allGroup.setFollowingUserInfoList(userInfoList);

        // 构造结果
        List<FollowingGroup> result = new ArrayList<>();
        result.add(allGroup);
        for (FollowingGroup followingGroup : groupLsit) {
            List<UserInfo> infoList = new ArrayList<>();
            for (UserFollowing userFollowing : list) {
                if(followingGroup.getId().equals(userFollowing.getGroupId())){
                    infoList.add(userFollowing.getUserInfo());
                }
            }
            followingGroup.setFollowingUserInfoList(infoList);
            result.add(followingGroup);
        }
        return result;
    }


    /**
     * 获取用户粉丝列表
     * @param userId 用户id
     * @return 粉丝列表
     */
    public List<UserFollowing> getUserFans(Long userId){
        // 获取关注当前用户的用户信息
        List<UserFollowing> fanList = userFollowingDao.getUserFans(userId);
        Set<Long> fanIdSet = fanList.stream().map(UserFollowing::getFollowingId).collect(Collectors.toSet());
        List<UserInfo> userInfoList = new ArrayList<>();
        if(fanIdSet != null && fanIdSet.size() > 0){
            userInfoList = userService.getUserByUserIds(fanIdSet);
        }
        // 获取当前用户关注的用户信息
        List<UserFollowing> followingList = userFollowingDao.getUserFollowing(userId);
        for (UserFollowing fan : fanList) {
            for (UserInfo user : userInfoList) {
                if(fan.getUserId().equals(user.getUserId())){
                    user.setFollowed(false);
                    fan.setUserInfo(user);
                }
            }
//            判断是否相互关注
            for (UserFollowing userFollowing : followingList) {
                if(fan.getUserId().equals(userFollowing.getFollowingId())){
                    fan.getUserInfo().setFollowed(true);
                }
            }
        }
        return fanList;
    }

    /**
     * 新增用户关注分组
     * @param followingGroup 关注分组
     * @return 关注分组id
     */
    public Long addUserFollowingGroups(FollowingGroup followingGroup) {
        followingGroup.setCreateTime(new Date());
        followingGroup.setType(UserConstant.USER_FOLLOWING_GROUP_TYPE_USER);
        followGroupService.addFollowingGroup(followingGroup);
        return followingGroup.getId();
    }


    /**
     * 判断是否关注了其中的某些用户
     * @param list 用户列表
     * @param userId 当前用户id
     * @return 用户猎豹
     */
    public List<UserInfo> checkFollowingStatus(List<UserInfo> list, Long userId) {
        List<UserFollowing> userFollowing = userFollowingDao.getUserFollowing(userId);
        for (UserInfo userInfo : list) {
            userInfo.setFollowed(false);
            for (UserFollowing following : userFollowing) {
                if(userInfo.getUserId().equals(following.getFollowingId())){
                    userInfo.setFollowed(true);
                }
            }
        }
        return list;
    }
}
