package cn.zeroeden.service.util;

import cn.zeroeden.domain.exception.ConditionException;
import com.github.tobato.fastdfs.domain.fdfs.FileInfo;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * @author: Zero
 * @time: 2023/5/25
 * @description:
 */

@Component
public class FastDFSUtil {

    @Resource
    private FastFileStorageClient fastFileStorageClient;

    @Resource
    private AppendFileStorageClient appendFileStorageClient;


    @Resource
    private RedisTemplate<String, String> redisTemplate;

    public static final String DEFAULT_GROUP = "group1";

    public static final String PATH_KEY = "path-key:";

    public static final String UPLOADED_SIZE_KEY = "uploaded-size-key:";

    public static final String UPLOADED_NO_KEY = "uploaded-no-key:";

    public static final int SLICE_SIZE = 1024 * 1024 * 2;


    public String getFileType(MultipartFile file){
        if(file == null){
            throw new ConditionException("非法文件！");
        }
        String fileName = file.getOriginalFilename();
        int index = fileName.lastIndexOf(".");
        return fileName.substring(index + 1);
    }

    /**
     * 上传通用小型文件
     * @param file 文件
     * @return 文件存储相对路径
     * @throws Exception 异常
     */
    public String uploadCommonFile(MultipartFile file) throws Exception{
        Set<MetaData> metaDataSet = new HashSet<>();
        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), this.getFileType(file), metaDataSet);
        return storePath.getPath();
    }

    /**
     * 上传可以断点续传的文件（大型文件的第一个分片，具体怎么分又前端来负责）
     * @param file 第一个分片
     * @return 相对路径
     * @throws Exception 异常
     */
    public  String uploadAppendFile(MultipartFile file) throws Exception{
        String fileName = file.getOriginalFilename();
        int index = fileName.lastIndexOf(".");
        StorePath storePath = appendFileStorageClient.uploadAppenderFile(DEFAULT_GROUP, file.getInputStream(), file.getSize(), fileName.substring(index + 1));
        return storePath.getPath();
    }

    /**
     * 修改/更新 大型文件的分片
     * @param file 分片文件
     * @param filePath 相对路径
     * @param offset 偏移量
     * @throws Exception 异常
     */
    public void modifyAppenderFile(MultipartFile file, String filePath, long offset) throws Exception{
        appendFileStorageClient.modifyFile(DEFAULT_GROUP, filePath, file.getInputStream(), file.getSize(), offset);
    }


    /**
     * 大型文件分片存储
     * @param file 分配文件
     * @param fileMd5 文件MD5信息
     * @param sliceNo 第几片
     * @param totalSliceNo 总片数
     * @return 存储相对路径
     * @throws Exception 异常
     */
    public String uploadFileBySlices(MultipartFile file, String fileMd5, Integer sliceNo, Integer totalSliceNo) throws Exception {
        if(file == null || sliceNo == null || totalSliceNo == null ){
            throw new ConditionException("参数异常！");
        }

        String pathKey = PATH_KEY + fileMd5;
        String uploadedSizeKey = UPLOADED_SIZE_KEY + fileMd5;
        String uploadedNokey = UPLOADED_NO_KEY + fileMd5;
        String uploadedSizeStr = redisTemplate.opsForValue().get(uploadedSizeKey);
        Long uploadedSize = 0L;
        if(!StringUtils.isNullOrEmpty(uploadedSizeStr)){
            uploadedSize = Long.valueOf(uploadedSizeStr);
        }
        String fileType = this.getFileType(file);
        if(sliceNo == 1){ // 上传的是第一个分片
            String path = this.uploadAppendFile(file);
            if(StringUtils.isNullOrEmpty(path)){
                throw new ConditionException("上传失败！");
            }
            redisTemplate.opsForValue().set(pathKey, path);

            redisTemplate.opsForValue().set(uploadedNokey, "1");
        }else{
            String filePath = redisTemplate.opsForValue().get(pathKey);
            if(StringUtils.isNullOrEmpty(filePath)){
                throw new ConditionException("上传失败!");
            }
            this.modifyAppenderFile(file, filePath, uploadedSize);
            redisTemplate.opsForValue().increment(uploadedNokey);
        }
        uploadedSize += file.getSize();
        redisTemplate.opsForValue().set(uploadedSizeKey, String.valueOf(uploadedSize));
        // 如果所有分片全部上传完毕，则清空redis里面相关的key和value
        String uploadNoStr = redisTemplate.opsForValue().get(uploadedNokey);
        Integer uploadedNo = Integer.valueOf(uploadNoStr);
        String resultPath = "";
        if(uploadedNo.equals(totalSliceNo)){
            resultPath = redisTemplate.opsForValue().get(pathKey);
            List<String> keyList = Arrays.asList(uploadedSizeKey, pathKey, uploadedNokey);
            redisTemplate.delete(keyList);
        }
        return resultPath;
    }



    public File multipartFileToFile(MultipartFile file) throws Exception{
        String originalFileName = file.getOriginalFilename();
        String[] fileName = originalFileName.split("\\.");
        File f = File.createTempFile(fileName[0], "." + fileName[1]);
        file.transferTo(f);
        return f;
    }

    /**
     * 将文件切片
     * @param multipartFile 文件
     * @throws Exception 异常
     */
    public void converFileToSlices(MultipartFile multipartFile) throws Exception{
        String fileName = multipartFile.getOriginalFilename();
        String fileType = this.getFileType(multipartFile);
        File file = this.multipartFileToFile(multipartFile);
        long fileLength = file.length();
        int count = 1;
        for (int i = 0; i < fileLength; i += SLICE_SIZE) {
            // 这个File这个读取某个区间的二进制流
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            // 设置读取的区间开头偏移量
            randomAccessFile.seek(i);
            byte[] bytes = new byte[SLICE_SIZE];
            int len = randomAccessFile.read(bytes);
            String path = "/user/zero/file/" + count + "." + fileType;
            File slice = new File(path);
            FileOutputStream fos = new FileOutputStream(slice);
            fos.write(bytes, 0, len);
            fos.close();
            randomAccessFile.close();
            count++;


        }
        file.delete();
    }



    public void deleteFile(String filePath){
        fastFileStorageClient.deleteFile(filePath);
    }

    @Value("${fdfs.http.storage-addr}")
    private String fastHttpStorageAddr;


    public void viewVideoOnlineBySlices(HttpServletRequest request, HttpServletResponse response, String path) {
        FileInfo fileInfo = fastFileStorageClient.queryFileInfo(DEFAULT_GROUP, path);
        long totalFileSize = fileInfo.getFileSize();
        String url = fastHttpStorageAddr + path;
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, Object> headers = new HashMap<>();
        while (headerNames.hasMoreElements()){
            String header = headerNames.nextElement();
            headers.put(header, request.getHeader(header));
        }
        String rangeStr = request.getHeader("Range");
        String[] range;
        if(StringUtils.isNullOrEmpty(rangeStr)){
            rangeStr = "bytes=0-" + (totalFileSize - 1);
        }
        range = rangeStr.split("bytes=|-");
        long begin = 0;
        if(range.length >= 2){
            begin = Long.parseLong(range[1]);
        }
        long end = totalFileSize - 1;
        if(range.length >= 3){
            end = Long.parseLong(range[2]);
        }
        long len = (end - begin) + 1;
        String contentRange = "bytes" + begin + "-" + end + "/" + totalFileSize;
        response.setHeader("Content-Range",contentRange);
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Content-Type", "video/mp4");
        response.setContentLength((int)len);
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        // 发HTTP请求,这里暂时不做
    }
}
