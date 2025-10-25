package com.rempms.rempms_notification_service.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rempms.rempms_notification_service.dto.email.EmailLogDTO;
import com.rempms.rempms_notification_service.dto.email.EmailRequestDTO;
import com.rempms.rempms_notification_service.enums.Status;
import com.rempms.rempms_notification_service.model.EmailLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailLogMapper {

    private final ObjectMapper objectMapper;
    private final EmailHasAttachmentMapper emailHasAttachmentMapper;

    public EmailLog toEntity(EmailLog emailLog, EmailRequestDTO dto) {

        try {
            String logJson = objectMapper.writeValueAsString(dto);
            emailLog.setPayload(logJson);

        } catch (Exception e) {
            log.error("Error while converting EmailRequestDTO to JSON string: {}", e.getMessage());
            return null;
        }

        emailLog.setStatus(Status.PENDING.getStatus());
        emailLog.setCreatedAt(LocalDateTime.now());
        emailLog.setSentAt(null);
        emailLog.setRetryAt(null);
        emailLog.setRetryCount(0);
        emailLog.setCreatedBy("Admin");
        return emailLog;
    }

    public EmailLogDTO mapToDTO(EmailLogDTO dto, EmailLog emailLog) {
        if (emailLog == null) {
            return null;
        }

        dto.setId(emailLog.getId());
        dto.setPayload(emailLog.getPayload());
        dto.setStatus(emailLog.getStatus());
        dto.setCreatedAt(emailLog.getCreatedAt());
        dto.setSentAt(emailLog.getSentAt());
        dto.setRetryAt(emailLog.getRetryAt());
        dto.setRetryCount(emailLog.getRetryCount());
        dto.setAttachmentLogDTOList(emailHasAttachmentMapper.mapToDTOs(emailLog.getEmailHasAttachments()));
        return dto;
    }
}
