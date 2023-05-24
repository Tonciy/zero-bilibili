package cn.zeroeden.service;

import cn.zeroeden.dao.UserRoleDao;
import cn.zeroeden.domain.auth.UserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: Zero
 * @time: 2023/5/23
 * @description:
 */

@Service
public class UserRoleService {

    @Resource
    private UserRoleDao userRoleDao;

    public List<UserRole> getUserRolesByUserId(Long userId) {
        return userRoleDao.getUserRolesByUserId(userId);
    }

    public Integer addUsrRole(UserRole userRole) {
        return userRoleDao.addUsrRole(userRole);
    }
}
