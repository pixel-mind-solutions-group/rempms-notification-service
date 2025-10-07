package com.rempms.rempms_notification_service.service;

import com.rempms.rempms_notification_service.dto.email.EmailRequestDTO;
import com.rempms.rempms_notification_service.model.EmailLog;

public interface EmailLogService {

    EmailLog createEmailLog(EmailRequestDTO dto);
}
