package cn.zeroeden.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: Zero
 * @time: 2023/5/26
 * @description:
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {

    private Long id;

    /**
     * 文件存储路径
     */
    private String url;

    /**
     * 文件类型
     */
    private String type;;

    /**
     * 文件md5唯一标识串
     */
    private String md5;

    private Date createTime;



}
