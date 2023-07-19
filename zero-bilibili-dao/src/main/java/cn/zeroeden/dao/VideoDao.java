package cn.zeroeden.dao;


import cn.zeroeden.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface VideoDao {
    Integer addVideos(Video video);

    Integer batchAddVideoTags(List<VideoTag> tagList);

    Integer pageCountVideos(Map<String, Object> params);

    List<Video> pageListVidoes(Map<String, Object> params);

    Video getVideoByVideoId(Long videoId);

    VideoLike getVideoLikeByVideoIdAndUserId(@Param("videoId") Long videoId,@Param("userId") Long userId);

    Integer addVideoLike(VideoLike videoLike);

    Integer deleteVideoLikeByUserIdAndVideoId(@Param("userId") Long userId,@Param("videoId") Long videoId);


    Long countVideoLikes(Long videoId);

    Video getVideoById(Long videoId);

    Integer deleteVideoCollectionByUserIdAndVideoId(@Param("userId") Long userId,
                                                   @Param("videoId") Long videoId);

    Integer addVideoCollection(VideoCollection videoCollection);

    Long countVideoCollections(Long videoId);

    VideoCollection getVideoCollectionByVideoIdAndUserID(@Param("videoId") Long videoId,
                                                         @Param("userId") Long userId);

    VideoCoin getVideoCoinByVideoIdAndUserId(@Param("videoId") Long videoId,
                                             @Param("userId") Long userId);

    Integer addVideoCoin(VideoCoin videoCoin);

    Integer updateVideoCoin(VideoCoin videoCoin);

    Long countVideoCoinsAmount(Long videoId);

    Integer addVideoComment(VideoComment videoComment);

    Integer pageCountVideoComments(Map<String, Object> params);

    List<VideoComment> pageListVideoComments(Map<String, Object> params);

    List<VideoComment> batchGetVideoCommentsByRootIds(List<Long> parentList);

    Video getVideoDetailById(Long videoId);

    VideoView getVieoView(Map<String, Object> params);

    Integer addVideoView(VideoView videoView);

    Integer getVideoViewCounts(Long videoId);
}
