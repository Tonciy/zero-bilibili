package cn.zeroeden.dao;

import cn.zeroeden.domain.FollowingGroup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: Zero
 * @time: 2023/5/18
 * @description:
 */

@Mapper
public interface FollowingGroupDao {
    FollowingGroup getById(Long id);

    FollowingGroup getByType(String type);

    List<FollowingGroup> getByUserId(Long userId);

    Integer addFollowingGroup(FollowingGroup followingGroup);

    List<FollowingGroup> getUserFollowingGroups(Long userId);
}
