package cn.zeroeden.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: Zero
 * @time: 2023/7/3
 * @description: 视频投币
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoCoin {

    private Long id;

    private Long userId;

    private Long videoId;

    private Integer amount;

    private Date createTime;

    private Date updateTime;

}
