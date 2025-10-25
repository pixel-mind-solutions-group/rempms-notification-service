package com.rempms.rempms_notification_service.function;

import com.rempms.rempms_notification_service.dto.email.EmailRequestDTO;
import com.rempms.rempms_notification_service.mapper.ErrorLogMapper;
import com.rempms.rempms_notification_service.model.ErrorLog;
import com.rempms.rempms_notification_service.repository.ErrorLogRepository;
import com.rempms.rempms_notification_service.service.EmailLogService;
import com.rempms.rempms_notification_service.service.EmailService;
import com.rempms.rempms_notification_service.util.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

/**
 * @author maleeshasa
 * @Date 2024/10/06
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class EmailFunction {

    private final EmailLogService emailLogService;
    private final EmailService emailService;
    private final ErrorLogRepository errorLogRepository;
    private final ErrorLogMapper errorLogMapper;

    /**
     * This Email function is allowed to send emails with or without attachments,
     * immediate or scheduled,
     * to, cc, bcc options,
     * HTML or plain text body,
     * kafka, rabbitmq or rest apis are also allowed to send emails.
     *
     * @return {@link CommonResponse} - sms response
     * @author maleeshasa
     */
    @Bean
    public Consumer<Flux<EmailRequestDTO>> email() {
        return flux -> flux
                // Start processing the first 5 emails concurrently (backpressure)
                // As soon as one finishes, it takes the next email from the queue
                // It always keeps a maximum of 5 active tasks at any time
                .flatMap(this::processEmail, 5)
                .doOnComplete(() -> log.info("All email events processed successfully"))
                .subscribe();
    }

    /**
     * Processes an incoming email request by performing the following steps:
     * <ul>
     *     <li>Logs the start of email processing.</li>
     *     <li>Creates a new email log entry in the database for tracking purposes.</li>
     *     <li>Sends the actual email via the configured email service (e.g., SMTP).</li>
     *     <li>Logs the success message once the email is sent.</li>
     *     <li>Handles any exceptions that occur during processing by:
     *         <ul>
     *             <li>Logging the error details.</li>
     *             <li>Saving the error information into the error log repository.</li>
     *             <li>Continuing the reactive stream without interruption.</li>
     *         </ul>
     *     </li>
     * </ul>
     *
     * <p>This method is fully reactive and non-blocking, ensuring efficient resource usage.
     * It returns a {@link Mono<Void>} to indicate completion without returning a specific result.</p>
     *
     * @param request {@link EmailRequestDTO} - the email request containing recipient details, subject, body, and attachments.
     * @return {@link Mono<Void>} - a reactive stream that completes when the email has been processed or the error has been logged.
     * @author maleeshasa
     */
    private Mono<Void> processEmail(EmailRequestDTO request) {
        log.info("EmailConsumer => Processing email for {}", request.getToEmails());

        return emailLogService.createEmailLog(request)
                .flatMap(createdEmailLog ->
                        emailService.sendEmail(request, createdEmailLog.getId())
                )
                .doOnNext(response ->
                        log.info("Email sent to {} - Status: {}", request.getToEmails(), response.getMessage())
                )
                .onErrorResume(e -> {
                    log.error("EmailConsumer.email() => Exception: {}", e.getMessage());
                    ErrorLog errorLog = errorLogMapper.mapToEntity(new ErrorLog(), request, e);
                    return errorLogRepository.save(errorLog)
                            .then(Mono.empty()); // Continue stream without breaking
                })
                .then(); // Convert to Mono<Void> so flatMap can handle multiple
    }
}
