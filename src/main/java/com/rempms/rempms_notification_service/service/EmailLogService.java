package com.rempms.rempms_notification_service.service;

import com.rempms.rempms_notification_service.dto.email.EmailRequestDTO;
import com.rempms.rempms_notification_service.model.EmailLog;
import reactor.core.publisher.Mono;

/**
 * @author maleeshasa
 * @since 2025/10/06
 */
public interface EmailLogService {

    Mono<EmailLog> createEmailLog(EmailRequestDTO dto);
}
