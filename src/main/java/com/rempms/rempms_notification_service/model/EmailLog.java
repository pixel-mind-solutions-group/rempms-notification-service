package com.rempms.rempms_notification_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "email_log")
@Entity
public class EmailLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    @Column(nullable = false)
    private String payload;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime sentAt;

    @Column(nullable = false)
    private String createdBy;

    private LocalDateTime retryAt;

    @Column(nullable = false)
    private Integer retryCount;

    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "emailLog", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EmailHasAttachment> emailHasAttachments = new ArrayList<>();
}
