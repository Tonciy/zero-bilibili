package cn.zeroeden.service;

import cn.zeroeden.dao.DanmuDao;
import cn.zeroeden.domain.Danmu;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: Zero
 * @time: 2023/7/10
 * @description:
 */

@Service
public class DanmuService {

    private static final String DANMU_KEY = "dm-video-";

    @Resource
    private DanmuDao danmuDao;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    public List<Danmu> getDanmus(Map<String, Object> params){
        return danmuDao.getDanmus(params);
    }

    /**
     * 优先查Redis，没有的话查数据库，并把数据写入到Redis中
     * @param videoId
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Danmu> getDanmus(Long videoId, String startTime, String endTime) throws ParseException {
        String key  = DANMU_KEY + videoId;
        String value = redisTemplate.opsForValue().get(key);
        List<Danmu> list;
        if(!StringUtils.isNullOrEmpty(value)){
            list = JSONArray.parseArray(value, Danmu.class);
            if(!StringUtils.isNullOrEmpty(startTime)
               && !StringUtils.isNullOrEmpty(endTime)){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date startDate = sdf.parse(startTime);
                Date endDate = sdf.parse(endTime);
                List<Danmu> childList = new ArrayList<>();
                // 判断时间
                for (Danmu danmu : list) {
                    Date createTime = danmu.getCreateTime();
                    if(createTime.after(startDate) && createTime.before(endDate)){
                        childList.add(danmu);
                    }
                }
                list = childList;
            }
        }else{
            Map<String, Object> params = new HashMap<>();
            params.put("videoId", videoId);
            params.put("startTime", startTime);
            params.put("endTime", endTime);
            list = danmuDao.getDanmus(params);
            // 保存弹幕到Redis中
            redisTemplate.opsForValue().set(key, JSONObject.toJSONString(list));
        }
        return list;
    }

    public void  addDanmu(Danmu danmu){
        danmuDao.addDanmu(danmu);
    }
    @Async
    public void  asyncAddDanmu(Danmu danmu){
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
