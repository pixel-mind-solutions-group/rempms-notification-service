package com.rempms.rempms_notification_service.service.rest.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rempms.rempms_notification_service.client.MessageServiceClient;
import com.rempms.rempms_notification_service.dto.email.EmailRequestDTO;
import com.rempms.rempms_notification_service.dto.email.EmailResponseDTO;
import com.rempms.rempms_notification_service.exception.FeignCustomException;
import com.rempms.rempms_notification_service.service.rest.MessageClientService;
import com.rempms.rempms_notification_service.util.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageClientServiceImpl implements MessageClientService {

    private final MessageServiceClient messageServiceClient;
    private final ObjectMapper objectMapper;

    @Override
    public EmailResponseDTO sendEmail(EmailRequestDTO dto) {
        log.info("MessageClientServiceImpl.sendEmail() => started.");

        try {
            ResponseEntity<CommonResponse> response = messageServiceClient.sendEmail(dto);

            if (response.getBody() != null &&
                    response.getBody().getStatus().equals(HttpStatus.OK) &&
                    response.getBody().getData() != null) {

                log.info("Email sent successfully.");
                return objectMapper.convertValue(response.getBody().getData(), EmailResponseDTO.class);

            } else {
                log.error("Failed to send email. Response: {}", response);
                return null;
            }

        } catch (FeignCustomException e) {
            log.error("Error occurred while calling message service: {}", e.getMessage());
            return null;
        }
    }
}
