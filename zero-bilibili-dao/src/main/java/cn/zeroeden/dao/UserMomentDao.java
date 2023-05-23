package cn.zeroeden.dao;


import cn.zeroeden.domain.UserMoment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMomentDao {
    Integer addUserMoments(UserMoment userMoment);
}
