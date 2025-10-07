package com.rempms.rempms_notification_service.dto.email;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmailResponseDTO {
    private Boolean success;
    private LocalDateTime sentAt;
}
