package com.rempms.rempms_notification_service.service.impl;

import com.rempms.rempms_notification_service.EmailLogRepository;
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

@RequiredArgsConstructor
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Value("${email.retry.count}")
    private Integer retryCount;

    private final MessageClientService messageClientService;
    private final EmailLogRepository emailLogRepository;
    private final EmailLogMapper emailLogMapper;

    @Override
    public CommonResponse sendEmail(EmailRequestDTO dto, Integer logId) {

        Optional<EmailLog> emailLog = emailLogRepository.findById(logId);

        if (emailLog.isPresent()) {

            EmailResponseDTO response = messageClientService.sendEmail(dto);

            if (response != null && response.getSuccess()) {
                emailLog.get().setSentAt(response.getSentAt());
                emailLog.get().setStatus(Status.SENT.getStatus());

            } else if (emailLog.get().getRetryCount() < 3) {
                emailLog.get().setStatus(Status.RETRIED.getStatus());

            } else {
                emailLog.get().setStatus(Status.FAILED.getStatus());
            }

        } else {
            log.error("EmailServiceImpl.sendEmail() => Email log not found for id: {}", logId);
        }

        return new CommonResponse(
                HttpStatus.OK, "Email processed",
                emailLogMapper.mapToDTO(new EmailLogDTO(), emailLogRepository.save(emailLog.get())
                )
        );
    }
}
