package cn.zeroeden.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: Zero
 * @time: 2023/5/23
 * @description: 用户资源(资源类别比较多，用这个来装载)
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAuthorities {

    /**
     * 页面元素资源集合
     */
    private List<AuthRoleElementOperation> roleElementOperationList;

    /**
     * 菜单资源集合
     */
    private List<AuthRoleMenu> roleMenuList;
}
