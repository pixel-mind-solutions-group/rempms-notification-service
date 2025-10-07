package com.rempms.rempms_notification_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "email_has_attachment")
@Entity
public class EmailHasAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String originalFileName;

    private String attachmentBase64;

    private String contentType;

    @JoinColumn(name = "email_log_id", referencedColumnName = "id")
    @ManyToOne
    private EmailLog emailLog;
}
