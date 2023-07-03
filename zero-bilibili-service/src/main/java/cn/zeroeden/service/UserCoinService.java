package cn.zeroeden.service;

import cn.zeroeden.dao.UserCoinDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author: Zero
 * @time: 2023/7/3
 * @description:
 */

@Service
public class UserCoinService {
    @Resource
    private UserCoinDao userCoinDao;

    public Integer getUserCoinsAmountByUserId(Long userId) {
        return userCoinDao.countUserCoinsAmountByUserId(userId);
    }

    public void updateUserCoinAmountByUserId(Long userId, Integer amount) {
        Date updatTime = new Date();
        userCoinDao.updateUserCoinAmountByUserId(userId, amount, updatTime);
    }
}
