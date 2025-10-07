package com.rempms.rempms_notification_service.service;

import com.rempms.rempms_notification_service.dto.email.EmailRequestDTO;
import com.rempms.rempms_notification_service.util.CommonResponse;

public interface EmailService {

    CommonResponse sendEmail(EmailRequestDTO dto, Integer logId);
}
