package cn.zeroeden.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: Zero
 * @time: 2023/5/23
 * @description: 页面元素操作
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthElementOperation {
    private Long id;
    /**
     * 页面元素名称
     */
    private String elementName;
    /**
     * 页面元素唯一编码
     */
    private String elementCode;
    /**
     *  操作类型：0：可点击，1：可见
     */
    private String operationType;
    private Date createTime;
    private Date updateTime;

}
