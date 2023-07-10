package cn.zeroeden.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: Zero
 * @time: 2023/7/10
 * @description:
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Danmu {

    private Long id;


    private Long userId;

    private Long videoId;

    private String content;

    private String danmuTime;


    private Date createTime;




}
