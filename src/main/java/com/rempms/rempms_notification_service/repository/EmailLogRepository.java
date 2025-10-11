package com.rempms.rempms_notification_service.repository;

import com.rempms.rempms_notification_service.model.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog, Integer> {
}
