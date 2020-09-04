package com.moxi.mogublog.picture.config;

import com.baomidou.mybatisplus.extension.api.IErrorCode;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.moxi.mougblog.base.global.ECode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 通用 Api Controller 全局异常处理
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * <p>
     * 自定义 REST 业务异常
     * <p>
     *
     * @param e 异常类型
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public R<Object> handleBadRequest(Exception e) {
        /*
         * 业务逻辑异常
         */
        if (e instanceof ApiException) {
            IErrorCode errorCode = ((ApiException) e).getErrorCode();
            if (null != errorCode) {
                logger.debug("Rest request error, {}", errorCode.toString());
                return R.failed(errorCode);
            }
            logger.debug("Rest request error, {}", e.getMessage());
            return R.failed(e.getMessage());
        }

        /*
         * 参数校验异常
         */
        if (e instanceof BindException) {
            BindingResult bindingResult = ((BindException) e).getBindingResult();
            if (null != bindingResult && bindingResult.hasErrors()) {
                List<Object> jsonList = new ArrayList<>();
                bindingResult.getFieldErrors().stream().forEach(fieldError -> {
                    Map<String, Object> jsonObject = new HashMap<>(2);
                    jsonObject.put("name", fieldError.getField());
                    jsonObject.put("msg", fieldError.getDefaultMessage());
                    jsonList.add(jsonObject);
                });
                return R.restResult(jsonList, ApiErrorCode.FAILED);
            }
        }

        // 判断是否是空指针异常
        if (e instanceof NullPointerException) {
            logger.error("Error: handleBadRequest StackTrace : {}", e);
            R r = new R();
            r.setCode(ECode.SERVER_ERROR);
            r.setMsg("系统内部出现空指针异常，请到后台查看日志记录");
            return r;
        }

        /**
         * 系统内部异常，打印异常栈
         */
        logger.error("Error: handleBadRequest StackTrace : {}", e);
        R r = new R();
        r.setCode(ECode.SERVER_ERROR);
        r.setMsg("系统内部出现异常，请到后台查看日志记录");
        return r;
    }
}
