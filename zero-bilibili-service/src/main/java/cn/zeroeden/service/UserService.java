package cn.zeroeden.service;

import cn.zeroeden.dao.UserDao;
import cn.zeroeden.domain.PageResult;
import cn.zeroeden.domain.User;
import cn.zeroeden.domain.UserInfo;
import cn.zeroeden.domain.auth.RefreshTokenDetail;
import cn.zeroeden.domain.constant.UserConstant;
import cn.zeroeden.domain.exception.ConditionException;
import cn.zeroeden.service.util.MD5Util;
import cn.zeroeden.service.util.RSAUtil;
import cn.zeroeden.service.util.TokenUtil;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author: Zero
 * @time: 2023/5/17
 * @description:
 */

@Service
public class UserService {

    @Resource
    private UserDao userDao;


    @Resource
    private UserAuthService userAuthService;
    /**
     * 注册 用户账号和用户个人信息
     * @param user 用户账号信息
     */
    public void addUser(User user) {
        // 1. 手机号码校验
        String phone = user.getPhone();
        if(StringUtils.isNullOrEmpty(phone)){
            throw new ConditionException("手机号不能为空！");
        }
        User dbUser = this.getUserByPhone(phone);
        if(dbUser != null){
            throw new ConditionException("该手机号已经被注册！");
        }
        // 2. 默认让当前时间为salt值
        Date now = new Date();
        String salt = String.valueOf(now.getTime());
        // 3. 密码RSA解密 + 拼接 + MD5加密
        String password = user.getPassword();
        String rawPassword = "";
        try {
            rawPassword = RSAUtil.decrypt(password);
        }catch (Exception e){
            throw new ConditionException("密码解密失败！");
        }
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        // 4. 注册用户账号
        user.setSalt(salt);
        user.setPassword(md5Password);
        user.setCreateTime(now);
        userDao.addUser(user);
        // 5. 注册用户个人信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setBirth(UserConstant.DEFAULT_BIRTH);
        userInfo.setCreateTime(now);
        userInfo.setNick(UserConstant.DEFAULT_NICK);
        userInfo.setGender(UserConstant.GENDER_MALE);
        userDao.addUserInfo(userInfo);
        // 6. 添加默认角色
        userAuthService.addUserDefaultRole(user.getId());
    }

    /**
     * 根据手机号查询用户账号
     * @param phone 手机号
     * @return 用户账号
     */
    public User getUserByPhone(String phone){
        if(StringUtils.isNullOrEmpty(phone)){
            return null;
        }
        return userDao.getUserByPhone(phone);
    }

    /**
     * 登录
     * @param user 用户账号
     * @return token令牌
     */
    public String login(User user) throws Exception {
        String phone = user.getPhone();
        if(StringUtils.isNullOrEmpty(phone)){
            throw new ConditionException("手机号码不能为空！");
        }
        User dbUser = this.getUserByPhone(phone);
        if(dbUser == null){
            throw new ConditionException("当前用户不存在！");
        }
        String salt = dbUser.getSalt();
        String password = user.getPassword();
        String rawPassword = "";
        try {
            rawPassword = RSAUtil.decrypt(password);
        }catch (Exception e){
            throw new ConditionException("密码解密失败！");
        }
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        if(StringUtils.isNullOrEmpty(dbUser.getPassword()) &&!dbUser.getPassword().equals(md5Password)){
            throw new ConditionException("密码错误！");
        }
        String token = TokenUtil.generateToken(dbUser.getId());
        return token;
    }

    public User getUserInfo(Long userId) {
        User user = userDao.getUserById(userId);
        UserInfo userInfo = userDao.getUserInfoByUserId(userId);
        user.setUserInfo(userInfo);
        return user;
    }

    public void updateUserInfosByUserId(UserInfo userInfo) {
        userInfo.setUpdateTime(new Date());
        userDao.updateUserInfosByUserId(userInfo);
    }

    public void updateUsersById(User user) throws Exception{
        Long id = user.getId();
        User dbUser = userDao.getUserById(id);
        if(dbUser == null){
            throw new ConditionException("用户不存在！");
        }
        // 用户可能改的是密码
        if(!StringUtils.isNullOrEmpty(user.getPassword())){
            String rawPassword = RSAUtil.decrypt(user.getPassword());
            String md5Password = MD5Util.sign(rawPassword, dbUser.getSalt(), "UTF-8");
            user.setPassword(md5Password);
        }
        user.setUpdateTime(new Date());
        userDao.updateUsersById(user);
    }

    public User getUserById(Long followingId) {
        return userDao.getUserById(followingId);
    }

    public List<UserInfo> getUserByUserIds(Set<Long> followingIds) {
        return userDao.getUserByUserIds(followingIds);
    }

    public PageResult<UserInfo> pageListUserInfos(JSONObject params) {
        Integer no = params.getInteger("no");
        Integer size = params.getInteger("size");
        params.put("start", (no - 1) * size);
        params.put("limit", size);
        Integer total = userDao.pageCountUserInfos(params);
        List<UserInfo> list = new ArrayList<>();
        if(total > 0){
            list = userDao.pageListUserInfos(params);
        }
        return new PageResult<>(total, list);
    }

    public Map<String, Object> loginForDts(User user) throws Exception {

        String phone = user.getPhone();
        if(StringUtils.isNullOrEmpty(phone)){
            throw new ConditionException("手机号码不能为空！");
        }
        User dbUser = this.getUserByPhone(phone);
        if(dbUser == null){
            throw new ConditionException("当前用户不存在！");
        }
        String salt = dbUser.getSalt();
        String password = user.getPassword();
        String rawPassword = "";
        try {
            rawPassword = RSAUtil.decrypt(password);
        }catch (Exception e){
            throw new ConditionException("密码解密失败！");
        }
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        if(StringUtils.isNullOrEmpty(dbUser.getPassword()) &&!dbUser.getPassword().equals(md5Password)){
            throw new ConditionException("密码错误！");
        }
        Long userId = dbUser.getId();
        String accessToken = TokenUtil.generateToken(userId);
        String refreshToken = TokenUtil.generateRefreshToken(userId);
        // 保存refresh token 到数据库
        userDao.deleteRefreshToken(refreshToken, userId);
        userDao.addRefreshToken(refreshToken, userId, new Date());
        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        return result;
    }

    public void logout(String refreshToken, Long userId) {
        userDao.deleteRefreshToken(refreshToken, userId);
    }

    public String refreshAccessToken(String refreshToken) throws Exception {
        RefreshTokenDetail refreshTokenDetail = userDao.getRefreshTokenDetail(refreshToken);
        if(refreshTokenDetail == null){
            // 不存在fefresh token，则禁止生成新的资源访问token
            // 此时返回的状态码应该与“555”区别开来，方便前端用拦截器处理请求，虽然都是access token过期
            //     但是555可以有机会再次获取新的access Token
            //     而556则直接禁止，前端可以直接提示token过期即可
            throw new ConditionException("556", "token过期");
        }
        Long userId = refreshTokenDetail.getUserId();
        return TokenUtil.generateToken(userId);
    }

    public List<UserInfo> batchGetUserInfoByUserIds(Set<Long> userIdList) {
        return userDao.batchGetUserInfoByUserIds(userIdList);
    }
}
