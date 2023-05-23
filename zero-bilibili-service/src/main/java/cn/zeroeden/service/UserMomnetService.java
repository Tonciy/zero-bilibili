package cn.zeroeden.service;

import cn.zeroeden.dao.UserMomentDao;
import cn.zeroeden.domain.UserMoment;
import cn.zeroeden.domain.constant.UserMomentConstant;
import cn.zeroeden.service.util.RocketMQUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.validator.Msg;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * @author: Zero
 * @time: 2023/5/23
 * @description: 用户动态
 */


@Service
public class UserMomnetService {

    @Resource
    private UserMomentDao userMomentDao;

    @Resource
    private DefaultMQProducer producer;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    public void addUserMoments(UserMoment userMoment) throws Exception {
        userMoment.setCreateTime(new Date());
        // 存储动态信息
        userMomentDao.addUserMoments(userMoment);
        // 发布动态
        Message message = new Message(UserMomentConstant.TOPIC_MOMENTS, JSONObject.toJSONString(userMoment).getBytes("UTF-8"));
        RocketMQUtil.syncSendMsg(producer, message);
    }

    public List<UserMoment> getUserSubscribedMoments(Long userId) {
        String key = "subscribed-" + userId;
        String listStr = redisTemplate.opsForValue().get(key);
        return JSONArray.parseArray(listStr, UserMoment.class);
    }
}
