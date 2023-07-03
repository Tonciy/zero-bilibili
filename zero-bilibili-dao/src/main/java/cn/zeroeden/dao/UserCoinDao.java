package cn.zeroeden.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface UserCoinDao {
    Integer countUserCoinsAmountByUserId(Long userId);

    Integer updateUserCoinAmountByUserId(@Param("userId") Long userId,
                                         @Param("amount") Integer amount,
                                         @Param("updateTime") Date updateTime);
}
