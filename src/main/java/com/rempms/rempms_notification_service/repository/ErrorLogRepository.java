package com.rempms.rempms_notification_service.repository;

import com.rempms.rempms_notification_service.model.ErrorLog;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author maleeshasa
 * @Date 2024/10/06
 */
@Repository
public interface ErrorLogRepository extends ReactiveCrudRepository<ErrorLog, Integer> {
}
