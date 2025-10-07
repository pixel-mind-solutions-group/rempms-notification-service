package com.rempms.rempms_notification_service.function;

import com.rempms.rempms_notification_service.dto.email.EmailRequestDTO;
import com.rempms.rempms_notification_service.model.EmailLog;
import com.rempms.rempms_notification_service.service.EmailLogService;
import com.rempms.rempms_notification_service.service.EmailService;
import com.rempms.rempms_notification_service.util.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class EmailFunction {

    private final EmailLogService emailLogService;
    private final EmailService emailService;

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

            EmailLog createdEmailLog = emailLogService.createEmailLog(request);

            if (createdEmailLog == null) {
                log.error("EmailFunction.email() => Error occurred while creating email log");
                return ResponseEntity.ok(
                        new CommonResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while creating email log", null
                        )
                );
            }

            log.info("EmailFunction.email() => sending email...");
            return ResponseEntity.ok(emailService.sendEmail(request, createdEmailLog.getId()));
        };
    }
}
