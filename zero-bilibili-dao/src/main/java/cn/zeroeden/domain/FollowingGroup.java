package cn.zeroeden.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author: Zero
 * @time: 2023/5/18
 * @description:
 */

@Data
@NoArgsConstructor
public class FollowingGroup {
    private Long id;
    private Long userId;
    private String name;
    /**
     * 关注分组类型：0：特别关注，1：悄悄关注，2：默认关注，3：自定义关注
     */
    private String type;
    private Date createTime;
    private Date updateTime;

    private List<UserInfo> followingUserInfoList;
}
