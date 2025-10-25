package com.rempms.rempms_notification_service.service.impl;

import com.rempms.rempms_notification_service.dto.email.EmailRequestDTO;
import com.rempms.rempms_notification_service.mapper.EmailHasAttachmentMapper;
import com.rempms.rempms_notification_service.mapper.EmailLogMapper;
import com.rempms.rempms_notification_service.model.EmailHasAttachment;
import com.rempms.rempms_notification_service.model.EmailLog;
import com.rempms.rempms_notification_service.repository.EmailHasAttachmentRepository;
import com.rempms.rempms_notification_service.repository.EmailLogRepository;
import com.rempms.rempms_notification_service.service.EmailLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author maleeshasa
 * @since 2025/10/06
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class EmailLogServiceImpl implements EmailLogService {

    private final EmailLogMapper emailLogMapper;
    private final EmailLogRepository emailLogRepository;
    private final EmailHasAttachmentMapper emailHasAttachmentMapper;
    private final EmailHasAttachmentRepository emailHasAttachmentRepository;

    /**
     * Creates a new email log entry along with its associated attachments in a reactive, non-blocking way.
     *
     * <p>This method performs the following steps:</p>
     * <ul>
     *     <li>Maps the incoming {@link EmailRequestDTO} into an {@link EmailLog} entity.</li>
     *     <li>Saves the {@link EmailLog} record to the database.</li>
     *     <li>Maps and saves all email attachments (if any) associated with the saved log entry.</li>
     *     <li>Logs success and error messages for better traceability.</li>
     * </ul>
     *
     * <p>All operations are executed reactively using R2DBC, ensuring that no thread blocking occurs during
     * database interactions. The returned {@link Mono} completes once the email log and its attachments
     * have been fully persisted.</p>
     *
     * @param dto {@link EmailRequestDTO} - The DTO containing email details such as recipients, subject, body, and attachments.
     * @return {@link Mono}&lt;{@link EmailLog}&gt; - A reactive Mono emitting the saved {@link EmailLog} entity upon successful persistence.
     * @throws RuntimeException if any error occurs during log or attachment saving (logged and propagated reactively).
     * @author maleeshasa
     */
    @Override
    public Mono<EmailLog> createEmailLog(EmailRequestDTO dto) {
        log.info("EmailLogServiceImpl.createEmailLog() => started");

        EmailLog emailLog = emailLogMapper.toEntity(new EmailLog(), dto);

        return emailLogRepository.save(emailLog)
                .flatMap(savedLog -> {
                    // map attachments after log is saved
                    List<EmailHasAttachment> attachments = emailHasAttachmentMapper.mapToEntities(dto.getAttachments(), savedLog);

                    return Flux.fromIterable(attachments)
                            .flatMap(emailHasAttachmentRepository::save)
                            .then(Mono.just(savedLog));
                })
                .doOnSuccess(saved -> log.info("EmailLog and attachments saved successfully"))
                .doOnError(error -> log.error("Error saving EmailLog or attachments", error));
    }
}
