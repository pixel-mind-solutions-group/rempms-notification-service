package com.rempms.rempms_notification_service.dto.sms;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SmsRequestDTO {
    private List<String> mobileNumbers = new ArrayList<>();
    private String sms;
    private String serviceProvider;
    private Integer priority;
    private LocalDateTime scheduleDateTime;
}
