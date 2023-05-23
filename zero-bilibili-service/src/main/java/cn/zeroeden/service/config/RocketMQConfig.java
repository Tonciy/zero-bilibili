package cn.zeroeden.service.config;

import cn.zeroeden.domain.FollowingGroup;
import cn.zeroeden.domain.UserFollowing;
import cn.zeroeden.domain.UserMoment;
import cn.zeroeden.domain.constant.UserMomentConstant;
import cn.zeroeden.service.UserFollowingService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Zero
 * @time: 2023/5/23
 * @description: RocketMQ 相关配置
 */

@Configuration
public class RocketMQConfig {

    @Value("${rocketmq.name.server.address}")
    private String nameServerAddr;

    @Autowired
    private RedisTemplate<String, String> resRedisTemplate;

    @Resource
    private UserFollowingService userFollowingService;


    @Bean("momentsProducer")
    public DefaultMQProducer momentsProducer() throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer(UserMomentConstant.GROUP_MOMENTS);
        producer.setNamespace(nameServerAddr);
        producer.start();
        return producer;
    }

    @Bean("momnetsConsumer")
    public DefaultMQPushConsumer momentsMqPushConsumer() throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(UserMomentConstant.GROUP_MOMENTS);
        consumer.setNamespace(nameServerAddr);
        consumer.subscribe(UserMomentConstant.TOPIC_MOMENTS, "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                MessageExt msg = list.get(0);
                if (msg == null) {
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                String bodyStr = new String(msg.getBody());
                // 将消息从byte数组转化成对应对象
                UserMoment userMoment = JSONObject.toJavaObject(JSONObject.parseObject(bodyStr), UserMoment.class);
                // 获取当前用户的所有粉丝
                List<UserFollowing> fanList = userFollowingService.getUserFans(userMoment.getUserId());
                // 将此动态添加到这些粉丝的待观看集合中
                for (UserFollowing fan : fanList) {
                    String key = "subscribed-"+ userMoment.getUserId();
                    String subscribedListStr = resRedisTemplate.opsForValue().get(key);
                    List<UserMoment> subscribedLsit;
                    if(StringUtils.isNullOrEmpty(subscribedListStr)){
                        // 待观看的数量为0
                        subscribedLsit = new ArrayList<>();
                    }else{
                        // 已经有待观看的动态了
                        subscribedLsit = JSONArray.parseArray(subscribedListStr, UserMoment.class);
                    }
                    subscribedLsit.add(userMoment);
                    resRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(subscribedLsit));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        return consumer;
    }
}
