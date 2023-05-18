package cn.zeroeden.domain.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Zero
 * @time: 2023/5/17
 * @description:
 */

@Data
@NoArgsConstructor
public class ConditionException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String code;

    public ConditionException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public ConditionException(String msg) {
        super(msg);
        this.code = "500";
    }

}
