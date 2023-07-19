package cn.zeroeden.service;

import cn.zeroeden.domain.Video;
import cn.zeroeden.repository.VideoRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: Zero
 * @time: 2023/7/19
 * @description:
 */

@Service
public class ElasticSearchService {

    @Resource
    private VideoRepository videoRepository;


    public void addVideo(Video video){
        videoRepository.save(video);
    }

    public Video getVideo(String keyword){
        return videoRepository.findByTitleLike(keyword);
    }

    public void deleteAllVideos(){
        videoRepository.deleteAll();
    }
}
