package cn.zeroeden.service;

import cn.zeroeden.dao.AuthRoleMenuDao;
import cn.zeroeden.domain.auth.AuthRoleMenu;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author: Zero
 * @time: 2023/5/24
 * @description:
 */

@Service
public class AuthRoleMenuService {


    @Resource
    private AuthRoleMenuDao authRoleMenuDao;
    public List<AuthRoleMenu> getAuthRoleMenusByRoleIds(Set<Long> roleIdSet) {
        return authRoleMenuDao.getAuthRoleMenusByRoleIds(roleIdSet);
    }
}
