package com.rempms.rempms_notification_service.dto.email;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class EmailLogDTO {
    private Integer id;
    private String payload;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private String createdBy;
    private LocalDateTime retryAt;
    private Integer retryCount;
    private String status;
    private List<EmailHasAttachmentDTO> attachmentLogDTOList;
}
