package com.rempms.rempms_notification_service.function;

import com.rempms.rempms_notification_service.constant.email.EmailCommonLogMessage;
import com.rempms.rempms_notification_service.dto.email.EmailRequestDTO;
import com.rempms.rempms_notification_service.enums.ApplicationSource;
import com.rempms.rempms_notification_service.exception.BaseException;
import com.rempms.rempms_notification_service.mapper.ErrorLogMapper;
import com.rempms.rempms_notification_service.model.EmailLog;
import com.rempms.rempms_notification_service.model.ErrorLog;
import com.rempms.rempms_notification_service.repository.ErrorLogRepository;
import com.rempms.rempms_notification_service.service.EmailLogService;
import com.rempms.rempms_notification_service.service.EmailService;
import com.rempms.rempms_notification_service.util.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.function.Function;

/**
 * @author maleeshasa
 * @Date 2024/11/16
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class EmailFunction {

    private final EmailLogService emailLogService;
    private final EmailService emailService;
    private final ErrorLogRepository errorLogRepository;
    private final ErrorLogMapper errorLogMapper;

    /**
     * This Email function is allowed to send emails with or without attachments,
     * immediate or scheduled,
     * to, cc, bcc options,
     * HTML or plain text body,
     * kafka, rabbitmq or rest apis are also allowed to send emails.
     *
     * @return {@link CommonResponse} - sms response
     * @author maleeshasa
     */
    @Bean
    public Function<EmailRequestDTO, ResponseEntity<CommonResponse>> email() {
        return request -> {
            log.info("EmailFunction.email() => started");

            EmailLog createdEmailLog;

            // validate application source
            if (!Arrays.stream(ApplicationSource.values())
                    .anyMatch(appSource -> appSource.getAppSource().equals(request.getApplicationSource()))) {
                throw new BaseException(HttpStatus.BAD_REQUEST.value(), "Invalid application source");
            }

            try {
                createdEmailLog = emailLogService.createEmailLog(request);

                if (createdEmailLog == null) {
                    log.error("EmailFunction.email() => Error occurred while creating email log");

                    // save error log
                    errorLogRepository.save(errorLogMapper.mapToEntity(new ErrorLog(), request, null));

                    return ResponseEntity.ok(
                            new CommonResponse(
                                    HttpStatus.INTERNAL_SERVER_ERROR, EmailCommonLogMessage.EMAIL_LOG_CREATE_ERROR, null
                            )
                    );
                }

            } catch (Exception e) {
                log.error("EmailFunction.email() => Exception: {}", e.getMessage());

                // save error log
                errorLogRepository.save(errorLogMapper.mapToEntity(new ErrorLog(), request, e));

                return ResponseEntity.ok(
                        new CommonResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR, EmailCommonLogMessage.EMAIL_LOG_CREATE_ERROR, null
                        )
                );
            }

            log.info("EmailFunction.email() => sending email...");
            return ResponseEntity.ok(emailService.sendEmail(request, createdEmailLog.getId()));
        };
    }
}
