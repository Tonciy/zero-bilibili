package cn.zeroeden.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: Zero
 * @time: 2023/5/23
 * @description: 用户发布的动态信息（）
 */

@Data
@NoArgsConstructor
public class UserMoment {
    private Long id;
    /**
     * 用户id（这条动态的拥有者）
     */
    private Long userId;
    /**
     * 动态类型：0：视频，1： 直播，2： 专栏童泰
     */
    private String type;
    /**
     * 内容详情id
     */
    private Long contentId;
    private Date createTime;
    private Date updateTime;
}
