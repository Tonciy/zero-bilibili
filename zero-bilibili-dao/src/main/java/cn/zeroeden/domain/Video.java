package cn.zeroeden.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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
@Document(indexName = "videos")
public class Video {

    @Id
    private Long id;
    @Field(type=FieldType.Long)
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
    @Field(type = FieldType.Text)
    private String title;
    /**
     * 类型： 0原创，1转载
     */
    @Field(type = FieldType.Text)
    private String type;
    /**
     * 视频时长
     */
    private String duration;
    /**
     * 分区：0：鬼畜，1：音乐，2：电影
     */
    private String area;
    @Field(type = FieldType.Text)
    private String description;


    /**
     * 标签列表
     */
    private List<VideoTag> videoTags;

    @Field(type = FieldType.Date)
    private Date createTime;
    @Field(type = FieldType.Date)
    private Date updateTime;





}
