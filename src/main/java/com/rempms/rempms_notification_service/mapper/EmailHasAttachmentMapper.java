package com.rempms.rempms_notification_service.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rempms.rempms_notification_service.dto.email.EmailHasAttachmentDTO;
import com.rempms.rempms_notification_service.model.EmailHasAttachment;
import com.rempms.rempms_notification_service.model.EmailLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailHasAttachmentMapper {

    private final ObjectMapper objectMapper;

    public EmailHasAttachment mapToEntity(EmailHasAttachment attachmentLog, EmailHasAttachmentDTO dto, EmailLog emailLog) {

        attachmentLog.setOriginalFileName(dto.getOriginalFileName());
        attachmentLog.setAttachmentBase64(dto.getAttachmentBase64());
        attachmentLog.setContentType(dto.getContentType());
        attachmentLog.setEmailLogId(emailLog.getId());
        return attachmentLog;
    }

    public EmailHasAttachmentDTO mapToDTO(EmailHasAttachmentDTO dto, EmailHasAttachment attachmentLog) {
        if (attachmentLog == null) {
            return null;
        }
        dto.setId(attachmentLog.getId());
        dto.setOriginalFileName(attachmentLog.getOriginalFileName());
        dto.setAttachmentBase64(attachmentLog.getAttachmentBase64());
        return dto;
    }

    public List<EmailHasAttachmentDTO> mapToDTOs(List<EmailHasAttachment> emailHasAttachments) {
        return emailHasAttachments.stream()
                .map(attachmentLog -> mapToDTO(new EmailHasAttachmentDTO(), attachmentLog))
                .toList();
    }


    public List<EmailHasAttachment> mapToEntities(List<EmailHasAttachmentDTO> emailHasAttachments, EmailLog emailLog) {
        return emailHasAttachments.stream()
                .map(hasAttachment -> mapToEntity(new EmailHasAttachment(), hasAttachment, emailLog))
                .toList();
    }
}
