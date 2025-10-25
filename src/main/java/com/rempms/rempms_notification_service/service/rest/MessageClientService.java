package com.rempms.rempms_notification_service.service.rest;

import com.rempms.rempms_notification_service.dto.email.EmailRequestDTO;
import com.rempms.rempms_notification_service.dto.email.EmailResponseDTO;
import reactor.core.publisher.Mono;

public interface MessageClientService {

    Mono<EmailResponseDTO> sendEmail(EmailRequestDTO dto);
}
