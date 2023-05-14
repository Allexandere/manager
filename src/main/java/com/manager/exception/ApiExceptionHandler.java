package com.manager.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice(basePackages = {"com.manager"})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiError> defaultErrorHandler(HttpServletRequest req, Exception e) {
        log.error(ExceptionUtils.getStackTrace(e));

        final ApiError.ApiErrorBuilder apiErrorBuilder = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .path(req.getContextPath() + req.getServletPath());

        if (e.getClass().getSuperclass().equals(ManagerException.class)) {
            int httpCode = e.getClass().getAnnotation(HttpErrorCode.class).value();
            return new ResponseEntity<>(apiErrorBuilder.message(e.getMessage()).build(),
                    HttpStatus.valueOf(httpCode));
        }

        if (e.getClass().equals(MissingServletRequestParameterException.class)) {
            return new ResponseEntity<>(apiErrorBuilder.message(e.getMessage()).build(),
                    HttpStatus.valueOf(400));
        }

        return new ResponseEntity<>(apiErrorBuilder.message(e.getMessage()).build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

