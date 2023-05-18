package cn.zeroeden.service.handler;

import cn.zeroeden.domain.JsonResponse;
import cn.zeroeden.domain.exception.ConditionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: Zero
 * @time: 2023/5/17
 * @description: 统一异常处理器
 */

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE) // 优先级最高
public class CommonGlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonResponse<String> commonExceptionHandler(HttpServletRequest request,
                                                       Exception e){
        String msg = e.getMessage();
        if(e instanceof ConditionException){
            String code = ((ConditionException) e).getCode();
            return new JsonResponse<>(code, msg);
        }else {
            return new JsonResponse<>("500", msg);
        }
    }
}
