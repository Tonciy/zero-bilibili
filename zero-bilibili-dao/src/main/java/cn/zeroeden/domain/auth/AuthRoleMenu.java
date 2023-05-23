package cn.zeroeden.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.Date;

/**
 * @author: Zero
 * @time: 2023/5/23
 * @description: 角色-菜单中间表
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthRoleMenu {

    private Long id;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 菜单id
     */
    private Long menuId;

    private Date createTime;

    /**
     * 具体菜单
     */
    private Menu menu;
}
