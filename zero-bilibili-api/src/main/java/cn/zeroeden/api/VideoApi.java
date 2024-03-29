package cn.zeroeden.api;

import cn.zeroeden.api.support.UserSupport;
import cn.zeroeden.domain.*;
import cn.zeroeden.service.UserService;
import cn.zeroeden.service.VideoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author: Zero
 * @time: 2023/5/26
 * @description:
 */


@RestController
public class VideoApi {

    @Resource
    private VideoService videoService;

    @Resource
    private UserSupport userSupport;

    /**
     * 投稿视频
     * @param video 视频
     * @return 状态值
     */
    @PostMapping("/videos")
    public JsonResponse<String> addVideos(@RequestBody Video video){
        Long userId = userSupport.getCurrentUserId();
        video.setUserId(userId);
        videoService.addVideo(video);
        return JsonResponse.success();
    }


    /**
     * 分页获取视频
     * @param size 一页展示数量
     * @param no 当前页码
     * @param area 分区类型
     * @return 分页结果
     */
    @GetMapping("/videos")
    public JsonResponse<PageResult<Video>> pageListVideos(Integer size,
                                                          Integer no,
                                                          String area){
        PageResult<Video> result = videoService.pageListVideos(size, no, area);
        return new JsonResponse<>(result);
    }


    /**
     * 下载视频分片
     * @param url 相对路径
     */
    @GetMapping("/video-slices")
    public void viewVideoOnlineBySlices(HttpServletRequest request,
                                        HttpServletResponse response,
                                        String url){
        videoService.viewVideoOnlineBySlices(request, response, url);
    }

    /**
     * 点赞视频
     * @param videoId 视频id
     * @return 状态值
     */
    @PostMapping("/video-likes")
    public JsonResponse<String> addVideoLikes(@RequestParam Long videoId){
        Long userId = userSupport.getCurrentUserId();
        videoService.addVideoLike(videoId, userId);
        return JsonResponse.success();
    }

    /**
     * 取消点赞视频
     * @param videoId 视频id
     * @return 状态值
     */
    @DeleteMapping("/video-likes")
    public JsonResponse<String> deleteVideoLike(@RequestParam Long videoId){
        Long userId = userSupport.getCurrentUserId();
        videoService.deleteVideoLike(videoId, userId);
        return JsonResponse.success();
    }


    /**
     * 查询视频点赞数量
     * @param videoId 视频id
     * @return 视频点赞数量以及当前操作者是否点过赞
     */
    @GetMapping("/video-likes")
    public JsonResponse<Map<String, Object>> getVideoLikes(@RequestParam Long videoId){
        Long userId = null;
        try {
            userId = userSupport.getCurrentUserId();
        }catch (Exception e){

        }
        Map<String, Object> result = videoService.getVideoLikes(videoId, userId);
        return new JsonResponse<>(result);
    }


    /**
     * 收藏视频
     * @param videoCollection 视频相关信息
     * @return 状态值
     */
    @PostMapping("/video-collections")
    public JsonResponse<String> addVideoCollection(@RequestBody VideoCollection videoCollection){
        Long userId = userSupport.getCurrentUserId();
        videoService.addVideoCollection(videoCollection,userId);
        return JsonResponse.success();
    }


    /**
     * 取消收藏视频
     * @param videoId 视频id
     * @return 状态值
     */
    @DeleteMapping("/video-collections")
    public JsonResponse<String> deleteVideoCollection(@RequestParam Long videoId){
        Long userId = userSupport.getCurrentUserId();
        videoService.delteVideoCollectionByUserIdAndVideoId(userId, videoId);
        return JsonResponse.success();
    }


    /**
     * 查询视频收藏数量
     * @param videoId
     * @return
     */
    @GetMapping("/video-collection")
    public JsonResponse<Map<String,Object>> getVideoCollections(@RequestParam Long videoId){
        Long userId = null;
        try {
            userId = userSupport.getCurrentUserId();
        }catch (Exception e){

        }
        Map<String, Object> result = videoService.getVideoCollectionsByVideoIdAndUserId(videoId, userId);
        return new JsonResponse<>(result);
    }


    /**
     * 视频投币
     * @param videoCoin 投币信息
     * @return 状态值
     */
    @PostMapping("/video-coins")
    public JsonResponse<String> addVideoCoins(@RequestBody VideoCoin videoCoin){
        Long userId = userSupport.getCurrentUserId();
        videoService.addVideoCoins(videoCoin, userId);
        return JsonResponse.success();
    }

    /**
     * 查询视频投币数量
     * @param videoId 视频id
     * @return 对应值
     */
    @GetMapping("/video-coins")
    public JsonResponse<Map<String, Object>> getVideoCoins(@RequestParam Long videoId){
        Long userId = null;
        try {
            userId = userSupport.getCurrentUserId();
        }catch (Exception e){

        }
        Map<String, Object> result = videoService.getVideoCoins(videoId, userId);
        return new JsonResponse<>(result);
    }


    /**
     * 添加视频评论
     * @param videoComment
     * @return
     */
    @PostMapping("/video-comments")
    public JsonResponse<String> addVideoComment(@RequestBody VideoComment videoComment){
        Long userId = userSupport.getCurrentUserId();
        videoService.addVideoComment(videoComment, userId);
        return JsonResponse.success();
    }

    /**
     * 分页查询视屏评论
     * @param size 一页数量
     * @param no 当前页码
     * @param videoId 视屏id
     * @return 分页数据
     */
    @GetMapping("/video-comments")
    public JsonResponse<PageResult<VideoComment>> pageListVideoComments(@RequestParam Integer size,
                                                                        @RequestParam Integer no,
                                                                        @RequestParam Long videoId){
        PageResult<VideoComment> result = videoService.pageListVideoComments(size, no, videoId);
        return new JsonResponse<>(result);
    }

    /**
     * 获取视频详情
     * @param videoId 视频id
     * @return 对应值
     */
    @GetMapping("/video-details")
    public JsonResponse<Map<String, Object>> getVideoDetails(Long videoId){
        Map<String, Object> result = videoService.getVideoDetails(videoId);
        return new JsonResponse<>(result);
    }

    /**
     * 添加视频观看记录
     * @param videoView 观看记录实体
     * @param request 额外信息
     * @return 状态值
     */
    public JsonResponse<String> addVideoView(@RequestBody VideoView videoView,
                                             HttpServletRequest request){
        Long userId;
        try {
            userId  = userSupport.getCurrentUserId();
            videoView.setUserId(userId);
            videoService.addVideoView(videoView, request);
        }catch (Exception e){
            videoService.addVideoView(videoView, request);

        }
        return JsonResponse.success();
    }

    /**
     * 查询视频播放量
     * @param videoId 视频id
     * @return 播放量
     */
    public JsonResponse<Integer> getVideoViewCounts(@RequestParam Long videoId){
        Integer count = videoService.getVideoViewCounts(videoId);
        return new JsonResponse<>(count);
    }
}
