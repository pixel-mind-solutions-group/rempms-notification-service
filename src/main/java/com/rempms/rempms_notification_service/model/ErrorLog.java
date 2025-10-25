package com.rempms.rempms_notification_service.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "error_log")
public class ErrorLog {
    @Id
    private Integer id;

    private String payload;

    private String error;

    private String errorType;

    private String stackTrace;

    private LocalDateTime createdAt;

    private String createdBy;
}
