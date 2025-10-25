package com.rempms.rempms_notification_service.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(name = "application_source")
public class ApplicationSource {
    @Id
    private Integer id;

    @Column
    private String source;
}
