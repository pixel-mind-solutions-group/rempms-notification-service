package com.rempms.rempms_notification_service.service.impl;

import com.rempms.rempms_notification_service.EmailLogRepository;
import com.rempms.rempms_notification_service.dto.email.EmailRequestDTO;
import com.rempms.rempms_notification_service.mapper.EmailLogMapper;
import com.rempms.rempms_notification_service.model.EmailLog;
import com.rempms.rempms_notification_service.service.EmailLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmailLogServiceImpl implements EmailLogService {

    private final EmailLogMapper emailLogMapper;
    private final EmailLogRepository emailLogRepository;

    @Override
    public EmailLog createEmailLog(EmailRequestDTO dto) {
        log.info("EmailLogServiceImpl.createEmailLog() => started");

        EmailLog mappedEntity = emailLogMapper.toEntity(new EmailLog(), dto);

        if (mappedEntity != null) {

            try {

                return emailLogRepository.save(mappedEntity);

            } catch (Exception e) {
                log.error("EmailLogServiceImpl.createEmailLog() => Exception occurred while saving email log: {}", e.getMessage());
                return null;
            }

        } else {
            log.error("EmailLogServiceImpl.createEmailLog() => mappedEntity is null");
            return null;
        }
    }
}
