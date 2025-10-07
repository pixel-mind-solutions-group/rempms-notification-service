package com.rempms.rempms_notification_service.service.rest;

import com.rempms.rempms_notification_service.dto.email.EmailRequestDTO;
import com.rempms.rempms_notification_service.dto.email.EmailResponseDTO;

public interface MessageClientService {

    EmailResponseDTO sendEmail(EmailRequestDTO dto);
}
