package com.rempms.rempms_notification_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "email_log")
@Entity
public class EmailLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String payload;

    private LocalDateTime createdAt;

    private LocalDateTime createdBy;

    private LocalDateTime retryAt;

    private Integer retryCount;

    private String status;
}
