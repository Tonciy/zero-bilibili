package cn.zeroeden.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: Zero
 * @time: 2023/7/19
 * @description: 视频观看统计记录
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoView {
    private Long id;

    private Long videoId;

    private Long userId;

    private String clientId;

    private String ip;

    private Date createTime;
}
