package cn.zeroeden.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author: Zero
 * @time: 2023/7/3
 * @description: 视频评论
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoComment {
    private Long id;

    private Long videoId;

    private Long userId;

    private String comment;

    private Long replyUserId;

    private Long rootId;

    private Date createTime;

    private Date updateTime;

    private List<VideoComment> childList;

    /**
     * 当前用户基本信息
     */
    private UserInfo userInfo;

    /**
     * 回复评论 用户的基本信息
     */
    private UserInfo replyUserInfo;
}
