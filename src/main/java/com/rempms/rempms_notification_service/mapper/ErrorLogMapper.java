package com.rempms.rempms_notification_service.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rempms.rempms_notification_service.dto.email.EmailRequestDTO;
import com.rempms.rempms_notification_service.model.ErrorLog;
import com.rempms.rempms_notification_service.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class ErrorLogMapper {

    public ErrorLog mapToEntity(ErrorLog errorLog, EmailRequestDTO request, Throwable e) {
        errorLog.setPayload(request != null ? request.toString() : null);
        errorLog.setError(e.getMessage());
        errorLog.setErrorType(e.getClass().getSimpleName());
        errorLog.setStackTrace(ExceptionUtil.getStackTraceAsString(e));
        errorLog.setCreatedAt(LocalDateTime.now());
        errorLog.setCreatedBy("Admin");
        return errorLog;
    }
}
