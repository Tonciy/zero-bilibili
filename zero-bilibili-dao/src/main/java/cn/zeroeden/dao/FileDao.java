package cn.zeroeden.dao;

import cn.zeroeden.domain.File;
import org.apache.ibatis.annotations.Mapper;



/**
 * @author: Zero
 * @time: 2023/5/26
 * @description:
 */


@Mapper
public interface FileDao {
    File getFileByMD5(String fileMd5);

    Integer addFile(File dbFileMD5);
}
