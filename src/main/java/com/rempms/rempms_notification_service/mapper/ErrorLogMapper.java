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

    private final ObjectMapper objectMapper;

    public ErrorLog mapToEntity(ErrorLog errorLog, EmailRequestDTO request, Exception e) {
        try {
            String payloadJson = objectMapper.writeValueAsString(request);
            errorLog.setPayload(payloadJson);

        } catch (Exception ex) {
            log.error("ErrorLogMapper.mapToEntity() => Exception: {}", ex.getMessage());
            return null;
        }

        errorLog.setError(e.getMessage());
        errorLog.setErrorType(e.getClass().getSimpleName());
        errorLog.setStackTrace(ExceptionUtil.getStackTraceAsString(e));
        errorLog.setCreatedAt(LocalDateTime.now());
        errorLog.setCreatedBy("Admin");
        return errorLog;
    }
}
