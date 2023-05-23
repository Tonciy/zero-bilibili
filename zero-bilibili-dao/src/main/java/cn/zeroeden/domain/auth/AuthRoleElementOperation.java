package cn.zeroeden.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: Zero
 * @time: 2023/5/23
 * @description: 角色-页面元素中间表
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRoleElementOperation {
    private Long id;
    private Long roleId;
    /**
     * 元素操作id
     */
    private Long elementOperationId;
    private Date createTime;
    /**
     * 具体元素
     */
    private AuthElementOperation authElementOperation;
}
