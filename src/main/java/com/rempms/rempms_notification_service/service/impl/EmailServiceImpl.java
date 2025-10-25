package com.rempms.rempms_notification_service.service.impl;

import com.rempms.rempms_notification_service.dto.email.EmailLogDTO;
import com.rempms.rempms_notification_service.dto.email.EmailRequestDTO;
import com.rempms.rempms_notification_service.enums.Status;
import com.rempms.rempms_notification_service.mapper.EmailLogMapper;
import com.rempms.rempms_notification_service.repository.EmailLogRepository;
import com.rempms.rempms_notification_service.service.EmailService;
import com.rempms.rempms_notification_service.service.rest.MessageClientService;
import com.rempms.rempms_notification_service.util.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
    public Mono<CommonResponse> sendEmail(EmailRequestDTO dto, Integer logId) {
        log.info("EmailServiceImpl.sendEmail() => started.");

        return emailLogRepository.findById(logId)
                .flatMap(emailLog ->
                        messageClientService.sendEmail(dto)
                                .flatMap(response -> {
                                    log.info("EmailServiceImpl.sendEmail() => Email sent successfully.");

                                    emailLog.setSentAt(response.getSentAt());
                                    emailLog.setStatus(Status.SENT.getStatus());

                                    return emailLogRepository.save(emailLog)
                                            .map(saved -> new CommonResponse(HttpStatus.OK, "Email sent successfully",
                                                    emailLogMapper.mapToDTO(new EmailLogDTO(), saved)));

                                })
                                .onErrorResume(e -> {
                                    log.error("EmailServiceImpl.sendEmail() => Error: {}", e.getMessage());

                                    emailLog.setStatus(Status.INIT_ATTEMPT_FAILED.getStatus());

                                    return emailLogRepository.save(emailLog)
                                            .map(saved -> new CommonResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                                                    "Email send failed", emailLogMapper.mapToDTO(new EmailLogDTO(), saved)));
                                })
                )
                .switchIfEmpty(Mono.just(new CommonResponse(HttpStatus.NOT_FOUND, "Email log not found", null)))
                .doFinally(signal -> log.info("EmailServiceImpl.sendEmail() => ended."));
    }
}
