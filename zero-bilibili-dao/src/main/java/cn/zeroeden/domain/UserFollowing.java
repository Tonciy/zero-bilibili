package cn.zeroeden.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: Zero
 * @time: 2023/5/18
 * @description:
 */

@Data
@NoArgsConstructor
public class UserFollowing {
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 关注用户id
     */
    private Long followingId;
    /**
     * 关注分组id
     */
    private Long groupId;
    private Date createTime;
    private UserInfo userInfo;

}
