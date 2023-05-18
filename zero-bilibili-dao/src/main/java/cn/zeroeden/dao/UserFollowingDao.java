package cn.zeroeden.dao;

import cn.zeroeden.domain.UserFollowing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserFollowingDao {
    Integer deleteUserFollowingByUserIdAndFollowingId(@Param("userId") Long userId, @Param("followingId") Long followingId);

    Integer addUserFollowing(UserFollowing userFollowing);

    List<UserFollowing> getUserFollowing(Long userId);

    List<UserFollowing> getUserFans(Long followingId);
}
