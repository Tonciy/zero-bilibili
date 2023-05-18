package cn.zeroeden.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author: Zero
 * @time: 2023/5/17
 * @description: 用户信息
 */

@Data
public class UserInfo {

    private Long id;
    private Long userId;
    /**
     * 昵称
     */
    private String nick;
    private String avatar;
    private String sign;
    /**
     * 0 男，1 女，2 未知
     */
    private String gender;
    private String birth;
    private Date createTime;
    private Date updateTime;
}
