package cn.zeroeden.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: Zero
 * @time: 2023/7/3
 * @description:
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoCollection {

    private Long id;

    private Long videoId;

    private Long userId;

    private Long groupId;

    private Date createTime;
}
