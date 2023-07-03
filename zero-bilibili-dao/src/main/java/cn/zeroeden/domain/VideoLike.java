package cn.zeroeden.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: Zero
 * @time: 2023/7/3
 * @description: 视频点赞
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoLike {

    private Long id;
    private Long videoId;
    private Long userId;
    private Date createTime;
}
