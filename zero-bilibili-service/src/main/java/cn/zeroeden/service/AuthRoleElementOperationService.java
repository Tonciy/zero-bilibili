package cn.zeroeden.service;

import cn.zeroeden.dao.AuthRoleElementOperationDao;
import cn.zeroeden.domain.auth.AuthRoleElementOperation;
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
public class AuthRoleElementOperationService {


    @Resource
    private AuthRoleElementOperationDao authRoleElementOperationDao;

    public List<AuthRoleElementOperation> getRoleElementOperationByRoleIds(Set<Long> roleIdSet) {
        return authRoleElementOperationDao.getRoleElementOperationByRoleIds(roleIdSet);
    }
}
