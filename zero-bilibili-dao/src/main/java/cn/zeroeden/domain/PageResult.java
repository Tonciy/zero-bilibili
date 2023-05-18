package cn.zeroeden.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: Zero
 * @time: 2023/5/18
 * @description:
 */

@Data
@NoArgsConstructor
public class PageResult<T> {
    private Integer total;
    private List<T> list;

    public PageResult(Integer total, List<T> list){
        this.total = total;
        this.list = list;
    }
}
