package cn.zeroeden.service;

import cn.zeroeden.domain.FollowingGroup;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: Zero
 * @time: 2023/5/18
 * @description:
 */

@Service
public class FollowingGroupService {
    @Resource
    private cn.zeroeden.dao.FollowingGroupDao followingGroupDao;

    public  List<FollowingGroup> getByUserId(Long userId) {
        return followingGroupDao.getByUserId(userId);
    }

    public FollowingGroup getById(Long id){
        return followingGroupDao.getById(id);
    }

    public FollowingGroup getByType(String type){
        return followingGroupDao.getByType(type);
    }


    public void addFollowingGroup(FollowingGroup followingGroup) {
        followingGroupDao.addFollowingGroup(followingGroup);
    }

    public List<FollowingGroup> getUserFollowingGroups(Long userId) {
        return followingGroupDao.getUserFollowingGroups(userId);
    }
}
