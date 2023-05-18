package cn.zeroeden.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author: Zero
 * @time: 2023/5/17
 * @description: 用户账号
 */
@Data
public class User {

    private Long id;
    private String phone;
    private String email;
    private String password;
    /**
     * 盐值
     */
    private String salt;
    private Date createTime;
    private Date updateTime;
    private UserInfo userInfo;
}
