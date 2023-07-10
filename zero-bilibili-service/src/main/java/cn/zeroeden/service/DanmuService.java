package cn.zeroeden.service;

import cn.zeroeden.dao.DanmuDao;
import cn.zeroeden.domain.Danmu;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: Zero
 * @time: 2023/7/10
 * @description:
 */

@Service
public class DanmuService {

    @Resource
    private DanmuDao danmuDao;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    public List<Danmu> getDanmus(Map<String, Object> params){
        return danmuDao.getDanmus(params);
    }

    public void  addDanmu(Danmu danmu){
        danmuDao.addDanmu(danmu);
    }

    public void addDanmusToRedis(Danmu danmu){
        String key = "danmu-video-" + danmu.getVideoId();
        String value = redisTemplate.opsForValue().get(key);
        List<Danmu> list = new ArrayList<>();
        if(!StringUtils.isNullOrEmpty(value)){
            list = JSONArray.parseArray(value, Danmu.class);
        }
        list.add(danmu);
        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(list));
    }
}
