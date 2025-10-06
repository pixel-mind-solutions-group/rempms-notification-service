package com.rempms.rempms_notification_service.dto.email;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EmailRequestDTO {
    private List<String> toEmails = new ArrayList<>();
    private List<String> ccEmails = new ArrayList<>();
    private List<String> bccEmails = new ArrayList<>();
    private List<byte[]> attachments = new ArrayList<>();
    private String subject;
    private String body;
    private LocalDateTime scheduleDateTime;
}
