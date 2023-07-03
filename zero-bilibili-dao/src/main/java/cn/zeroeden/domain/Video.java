package cn.zeroeden.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author: Zero
 * @time: 2023/5/26
 * @description:
 */


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Video {

    private Long id;
    private Long userId;
    /**
     * 视频链接
     */
    private String url;
    /**
     * 封面链接
     */
    private String thumbnail;
    /**
     * 标题
     */
    private String title;
    /**
     * 类型： 0原创，1转载
     */
    private String type;
    /**
     * 视频时长
     */
    private String duration;
    /**
     * 分区：0：鬼畜，1：音乐，2：电影
     */
    private String area;
    private String description;


    /**
     * 标签列表
     */
    private List<VideoTag> videoTags;

    private Date createTime;
    private Date updateTime;





}
