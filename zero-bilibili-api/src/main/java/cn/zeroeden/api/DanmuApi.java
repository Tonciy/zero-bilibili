package cn.zeroeden.api;

import cn.zeroeden.api.support.UserSupport;
import cn.zeroeden.domain.Danmu;
import cn.zeroeden.domain.JsonResponse;
import cn.zeroeden.service.DanmuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

/**
 * @author: Zero
 * @time: 2023/7/10
 * @description:
 */


@RestController()
public class DanmuApi {

    @Resource
    private DanmuService danmuService;

    @Resource
    private UserSupport userSupport;


    /**
     * 获取某个视频的弹幕
     * @param videoId 视频id
     * @param startTime 起始时间
     * @param endTime 结尾时间
     * @return 对应值
     */
    @GetMapping("/danmus")
    public JsonResponse<List<Danmu>> getDanmus(@RequestParam Long videoId,
                                               String startTime,
                                               String endTime) throws ParseException {
        List<Danmu> list = null;
        try {
            // 通过解析token来判断是游客模式还是用户登录模式
            userSupport.getCurrentUserId();
            // 用户登录模式下查找
            list = danmuService.getDanmus(videoId, startTime, endTime);
        }catch (Exception e){
            list = danmuService.getDanmus(videoId, null, null);
        }
        return new JsonResponse<>(list);
    }
}
