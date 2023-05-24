package cn.zeroeden.service;

import cn.zeroeden.domain.auth.AuthRoleElementOperation;
import cn.zeroeden.domain.auth.AuthRoleMenu;
import cn.zeroeden.domain.auth.UserAuthorities;
import cn.zeroeden.domain.auth.UserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: Zero
 * @time: 2023/5/23
 * @description:
 */

@Service
public class UserAuthService {


    @Resource
    private UserRoleService userRoleService;


    @Resource
    private AuthRoleService authRoleService;

    public UserAuthorities getUserAuthorities(Long userId) {
        // 获取所有角色信息
        List<UserRole> rolesList = userRoleService.getUserRolesByUserId(userId);
        Set<Long> roleIdSet = rolesList.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
        List<AuthRoleElementOperation> roleElementOperationList = authRoleService.getRoleElementOperationByRoleIds(roleIdSet);
        List<AuthRoleMenu> authRoleMenuList = authRoleService.getAuthRoleMenusByRoleIds(roleIdSet);
        UserAuthorities userAuthorities = new UserAuthorities();
        userAuthorities.setRoleMenuList(authRoleMenuList);
        userAuthorities.setRoleElementOperationList(roleElementOperationList);
        return userAuthorities;
    }
}
