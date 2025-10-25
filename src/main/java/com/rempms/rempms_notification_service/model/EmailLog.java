package com.rempms.rempms_notification_service.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "email_log")
public class EmailLog {
    @Id
    private Integer id;

    @Column
    private String payload;

    @Column
    private LocalDateTime createdAt;

    private LocalDateTime sentAt;

    @Column
    private String createdBy;

    private LocalDateTime retryAt;

    @Column
    private Integer retryCount;

    @Column
    private String status;

    @Transient // because we load it manually
    private List<EmailHasAttachment> emailHasAttachments = new ArrayList<>();
}
