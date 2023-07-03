package cn.zeroeden.service;

import cn.zeroeden.dao.VideoDao;
import cn.zeroeden.domain.*;
import cn.zeroeden.domain.exception.ConditionException;
import cn.zeroeden.service.util.FastDFSUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.locks.Condition;

/**
 * @author: Zero
 * @time: 2023/5/26
 * @description:
 */

@Service
public class VideoService {

    @Resource
    private VideoDao videoDao;


    @Resource
    private FastDFSUtil fastDFSUtil;

    @Resource
    private UserCoinService userCoinService;

    @Transactional
    public void addVideo(Video video) {
        Date now = new Date();
        video.setCreateTime(now);
        videoDao.addVideos(video);
        Long id = video.getId();
        List<VideoTag> tags = video.getVideoTags();
        tags.forEach(item ->{
            item.setCreateTime(now);
            item.setVideoId(id);
        });
        videoDao.batchAddVideoTags(tags);
    }

    public PageResult<Video> pageListVideos(Integer size, Integer no, String area) {
        if(size == null || no == null){
            throw  new ConditionException("参数异常！");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("start", (no - 1) * size);
        params.put("limit", size);
        params.put("area", area);
        List<Video> list = new ArrayList<>();
        Integer total = videoDao.pageCountVideos(params);
        if(total > 0){
            list = videoDao.pageListVidoes(params);
        }
        return new PageResult<>(total, list);

    }

    public void viewVideoOnlineBySlices(HttpServletRequest request, HttpServletResponse response, String url) {
        fastDFSUtil.viewVideoOnlineBySlices(request, response, url);
    }

    public void addVideoLike(Long videoId, Long userId) {
        Video video = videoDao.getVideoByVideoId(videoId);
        if(video == null){
            throw new ConditionException("非法视频！");
        }
        VideoLike videoLike = videoDao.getVideoLikeByVideoIdAndUserId(videoId, userId);
        if (videoLike != null) {
            throw new ConditionException("已经赞过！");
        }
        videoLike = new VideoLike();
        videoLike.setVideoId(videoId);
        videoLike.setUserId(userId);
        videoLike.setCreateTime(new Date());
        videoDao.addVideoLike(videoLike);
    }

    public void deleteVideoLike(Long videoId, Long userId) {
        videoDao.deleteVideoLikeByUserIdAndVideoId(userId, videoId);
    }

    public Map<String, Object> getVideoLikes(Long videoId, Long userId) {
        Long count = videoDao.countVideoLikes(videoId);
        VideoLike videoLike = videoDao.getVideoLikeByVideoIdAndUserId(videoId, userId);
        boolean like = videoLike != null;
        Map<String, Object> result = new HashMap<>();
        result.put("count", count);
        result.put("like", like);
        return result;

    }

    @Transactional
    public void addVideoCollection(VideoCollection videoCollection, Long userId) {
        Long videoId = videoCollection.getVideoId();
        Long groupId = videoCollection.getGroupId();
        if(videoCollection == null || groupId == null){
            throw new ConditionException("参数异常！");
        }
        Video video = videoDao.getVideoById(videoId);
        if(video == null){
            throw new ConditionException("非法视频！");
        }
        //删除原有视频收藏 （这个方法支持更新操作,所以才有下面的删除语句）
        videoDao.deleteVideoCollectionByUserIdAndVideoId(userId, videoId);
        // 添加新的视频收藏
        videoCollection.setUserId(userId);
        videoCollection.setCreateTime(new Date());
        videoDao.addVideoCollection(videoCollection);
    }

    public void delteVideoCollectionByUserIdAndVideoId(Long userId, Long videoId) {
        videoDao.deleteVideoCollectionByUserIdAndVideoId(userId,videoId);
    }

    public Map<String, Object> getVideoCollectionsByVideoIdAndUserId(Long videoId, Long userId) {
        Long count = videoDao.countVideoCollections(videoId);
        VideoCollection videoCollection = videoDao.getVideoCollectionByVideoIdAndUserID(videoId, userId);
        boolean like = videoCollection != null;
        Map<String, Object> result = new HashMap<>();
        result.put("count", count);
        result.put("like", like);
        return result;
    }

    @Transactional
    public void addVideoCoins(VideoCoin videoCoin, Long userId) {
        Long videoId = videoCoin.getVideoId();
        Integer amount = videoCoin.getAmount();
        if (videoId == null) {
            throw new ConditionException("参数异常！");
        }
        Video video = videoDao.getVideoByVideoId(videoId);
        if(video == null){
            throw new ConditionException("非法视频！");
        }
        // 查询当前用户是否有足够的硬币
        Integer userCoinsAmount = userCoinService.getUserCoinsAmountByUserId(userId);
        userCoinsAmount = userCoinsAmount == null? 0: userCoinsAmount;
        if(amount > userCoinsAmount){
            throw new ConditionException("硬币数量不足！");
        }
        // 查询当前登录用户对该视频已经投币了多少硬币
        VideoCoin dbVideoCoin = videoDao.getVideoCoinByVideoIdAndUserId(videoId, userId);

        if(dbVideoCoin == null){
            // 新增视频投币
            videoCoin.setUserId(userId);
            videoCoin.setCreateTime(new Date());
            videoDao.addVideoCoin(videoCoin);
        }else{
            // 后续补增投币
            Integer dbAmout = dbVideoCoin.getAmount();
            dbAmout += amount;
            videoCoin.setUserId(userId);
            videoCoin.setAmount(dbAmout);
            videoCoin.setUpdateTime(new Date());
            videoDao.updateVideoCoin(videoCoin);
        }
        // 更新用户具有的硬币数量
        userCoinService.updateUserCoinAmountByUserId(userId, (userCoinsAmount - amount));
    }

    public Map<String, Object> getVideoCoins(Long videoId, Long userId) {
        Long count = videoDao.countVideoCoinsAmount(videoId);
        VideoCoin videoCoin = videoDao.getVideoCoinByVideoIdAndUserId(videoId, userId);
        boolean like = videoCoin != null;
        Map<String, Object> result = new HashMap<>();
        result.put("count", count);
        result.put("like", like);
        return result;
    }
}
