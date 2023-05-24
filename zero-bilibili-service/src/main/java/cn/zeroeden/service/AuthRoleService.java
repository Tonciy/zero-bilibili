package cn.zeroeden.service;

import cn.zeroeden.dao.AuthRoleDao;
import cn.zeroeden.domain.auth.AuthRole;
import cn.zeroeden.domain.auth.AuthRoleElementOperation;
import cn.zeroeden.domain.auth.AuthRoleMenu;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author: Zero
 * @time: 2023/5/23
 * @description: 对角色关联的资源权限表联合操作
 */

@Service
public class AuthRoleService {

    @Resource
    private AuthRoleElementOperationService authRoleElementOperationService;

    @Resource
    private AuthRoleMenuService auAthRoleMenuService;


    @Resource
    private AuthRoleDao authRoleDao;
    public List<AuthRoleElementOperation> getRoleElementOperationByRoleIds(Set<Long> roleIdSet) {
        return authRoleElementOperationService.getRoleElementOperationByRoleIds(roleIdSet);
    }

    public List<AuthRoleMenu> getAuthRoleMenusByRoleIds(Set<Long> roleIdSet) {
        return auAthRoleMenuService.getAuthRoleMenusByRoleIds(roleIdSet);
    }

    public AuthRole getRoleByCode(String code) {
        return authRoleDao.getRoleByCode(code);
    }
}
