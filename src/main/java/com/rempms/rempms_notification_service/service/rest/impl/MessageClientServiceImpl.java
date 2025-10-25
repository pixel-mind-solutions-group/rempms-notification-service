package com.rempms.rempms_notification_service.service.rest.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rempms.rempms_notification_service.dto.email.EmailRequestDTO;
import com.rempms.rempms_notification_service.dto.email.EmailResponseDTO;
import com.rempms.rempms_notification_service.service.rest.MessageClientService;
import com.rempms.rempms_notification_service.util.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author maleeshasa
 * @Date 2025/10/06
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageClientServiceImpl implements MessageClientService {

    private final ObjectMapper objectMapper;

    @Qualifier("urlBasedWebClient")
    private final WebClient urlBasedWebClient;

    @Value("${message.service.base.url}")
    private String messageServiceBaseUrl;

    /**
     * Sends an email by invoking an external message service through a reactive WebClient call.
     *
     * <p>This method performs the following steps:</p>
     * <ul>
     *     <li>Initiates a POST request to the configured message service endpoint.</li>
     *     <li>Sends the {@link EmailRequestDTO} payload containing recipient details, subject, body, and attachments.</li>
     *     <li>Deserializes the response into a {@link CommonResponse} object.</li>
     *     <li>If the response indicates success ({@link HttpStatus#OK}), converts the response data into an {@link EmailResponseDTO}.</li>
     *     <li>Handles and logs any errors that occur during communication or response processing.</li>
     * </ul>
     *
     * <p>This method operates in a fully non-blocking, reactive manner using {@link WebClient} and returns
     * a {@link Mono} that emits the {@link EmailResponseDTO} upon successful email dispatch.</p>
     *
     * @param dto {@link EmailRequestDTO} - The email request containing recipient list, message content, and optional attachments.
     * @return {@link Mono}&lt;{@link EmailResponseDTO}&gt; - A reactive Mono that emits the email response from the message service.
     * @throws RuntimeException if the message service responds with a non-success status or if any network/serialization error occurs.
     * @author maleeshasa
     */
    @Override
    public Mono<EmailResponseDTO> sendEmail(EmailRequestDTO dto) {
        log.info("MessageClientServiceImpl.sendEmail() => started.");

        return urlBasedWebClient.post()
                .uri(messageServiceBaseUrl + "/api/message/email/v1/send")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(CommonResponse.class)
                .flatMap(response -> {

                    if (response.getStatus().equals(HttpStatus.OK) && response.getData() != null) {
                        log.info("Email sent successfully.");

                        EmailResponseDTO emailResponse = objectMapper.convertValue(response.getData(), EmailResponseDTO.class);
                        return Mono.just(emailResponse);

                    } else {
                        log.error("Failed to send email. Response: {}", response);
                        return Mono.error(new RuntimeException("Failed to send email"));
                    }
                })
                .onErrorResume(e -> {
                    log.error("Error occurred while calling message service: {}", e.getMessage());
                    return Mono.error(e);
                });
    }
}
