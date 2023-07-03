package cn.zeroeden.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: Zero
 * @time: 2023/5/26
 * @description: 标签
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    private Long id;

    private String name;


    private Date createTime;
}
