package cn.zeroeden.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: Zero
 * @time: 2023/5/26
 * @description: 视频 -标签
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoTag {

    private Long id;
    private Long videoId;
    private Long tagId;
    private Date createTime;
}
