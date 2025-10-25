package com.rempms.rempms_notification_service.service;

import com.rempms.rempms_notification_service.dto.email.EmailRequestDTO;
import com.rempms.rempms_notification_service.util.CommonResponse;
import reactor.core.publisher.Mono;

/**
 * @author maleeshasa
 * @since 2024/11/16
 */
public interface EmailService {

    /**
     * This method is used to send email and update the email log status
     *
     * @param dto   {@link EmailRequestDTO} - the email request dto
     * @param logId {@link Integer} - the email log id
     * @return {@link CommonResponse} - the email processed response
     * @author maleeshasa
     * @Date 2024/11/16
     */
    Mono<CommonResponse> sendEmail(EmailRequestDTO dto, Integer logId);
}
