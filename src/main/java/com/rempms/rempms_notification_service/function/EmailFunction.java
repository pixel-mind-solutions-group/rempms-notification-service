package com.rempms.rempms_notification_service.function;

import com.rempms.rempms_notification_service.dto.email.EmailRequestDTO;
import com.rempms.rempms_notification_service.util.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Slf4j
@Configuration
public class EmailFunction {

    @Bean
    public Function<EmailRequestDTO, CommonResponse> email() {
        return request -> {
            log.info("Email sent to: {}", request.toString());
            return new CommonResponse();
        };
    }
}
