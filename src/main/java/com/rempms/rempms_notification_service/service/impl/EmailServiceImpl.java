package com.rempms.rempms_notification_service.service.impl;

import com.rempms.rempms_notification_service.repository.EmailLogRepository;
import com.rempms.rempms_notification_service.constant.email.EmailCommonLogMessage;
import com.rempms.rempms_notification_service.dto.email.EmailLogDTO;
import com.rempms.rempms_notification_service.dto.email.EmailRequestDTO;
import com.rempms.rempms_notification_service.dto.email.EmailResponseDTO;
import com.rempms.rempms_notification_service.enums.Status;
import com.rempms.rempms_notification_service.mapper.EmailLogMapper;
import com.rempms.rempms_notification_service.model.EmailLog;
import com.rempms.rempms_notification_service.service.EmailService;
import com.rempms.rempms_notification_service.service.rest.MessageClientService;
import com.rempms.rempms_notification_service.util.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author maleeshasa
 * @Date 2024/11/16
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Value("${email.retry.count}")
    private Integer retryCount;

    private final MessageClientService messageClientService;
    private final EmailLogRepository emailLogRepository;
    private final EmailLogMapper emailLogMapper;

    private String message;

    /**
     * This method is used to send email and update the email log status
     *
     * @param dto   {@link EmailRequestDTO} - the email request dto
     * @param logId {@link Integer} - the email log id
     * @return {@link CommonResponse} - the email processed response
     * @author maleeshasa
     * @Date 2024/11/16
     */
    @Override
    public CommonResponse sendEmail(EmailRequestDTO dto, Integer logId) {
        log.info("EmailServiceImpl.sendEmail() => started.");

        Optional<EmailLog> emailLog = emailLogRepository.findById(logId);

        if (emailLog.isPresent()) {

            try {
                log.info("EmailServiceImpl.sendEmail() => Sending email to: {}", dto.getToEmails());
                EmailResponseDTO response = messageClientService.sendEmail(dto);

                if (response != null && response.getSuccess()) {
                    message = EmailCommonLogMessage.SENT_SUCCESS;
                    emailLog.get().setSentAt(response.getSentAt());
                    emailLog.get().setStatus(Status.SENT.getStatus());

                } else {
                    message = EmailCommonLogMessage.SENT_FAILED;
                    emailLog.get().setStatus(Status.INIT_ATTEMPT_FAILED.getStatus());
                }

            } catch (Exception e) {
                log.error("EmailServiceImpl.sendEmail() => Exception: {}", e);
                message = EmailCommonLogMessage.SENT_FAILED;
                emailLog.get().setStatus(Status.INIT_ATTEMPT_FAILED.getStatus());
            }

        } else {
            log.error("EmailServiceImpl.sendEmail() => Email log not found for id: {}", logId);
        }

        log.info("EmailServiceImpl.sendEmail() => ended.");
        return new CommonResponse(
                HttpStatus.OK,
                message,
                emailLogMapper.mapToDTO(
                        new EmailLogDTO(), emailLogRepository.save(emailLog.get())
                )
        );
    }
}
