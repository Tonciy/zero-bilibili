package cn.zeroeden.api;

import cn.zeroeden.domain.JsonResponse;
import cn.zeroeden.service.FileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author: Zero
 * @time: 2023/5/25
 * @description:
 */

@RestController
public class FileApi {

    @Resource
    private FileService fileService;




    @PostMapping("/md5files")
    public JsonResponse<String> getFileMD5(MultipartFile file) throws Exception {
        String fileMD5 = fileService.getFileMD5(file);
        return new JsonResponse<>(fileMD5);
    }



    @PutMapping("/file-slcies")
    public JsonResponse<String> uploadFileBySlices(MultipartFile file,
                                                   String fileMD5,
                                                   Integer sliceNo,
                                                   Integer totalSliceNo) throws Exception{
        String filePath = fileService.uploadFileBySlices(file, fileMD5, sliceNo, totalSliceNo);
        return new JsonResponse<>(filePath);
    }
}
