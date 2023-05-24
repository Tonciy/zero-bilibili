package cn.zeroeden.dao;


import cn.zeroeden.domain.auth.AuthRole;
import cn.zeroeden.domain.auth.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuthRoleDao {
    AuthRole getRoleByCode(String code);
}
