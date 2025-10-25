//package com.rempms.rempms_notification_service.client;
//
//import com.rempms.rempms_notification_service.dto.email.EmailRequestDTO;
//import com.rempms.rempms_notification_service.util.CommonResponse;
//import feign.Headers;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//@FeignClient(name = "message-service", url = "${feign.client.config.message-service.url}")
//@Headers("Content-Type: application/json")
//public interface MessageServiceClient {
//
//    @PostMapping(value = "/api/message/email/v1/send")
//    ResponseEntity<CommonResponse> sendEmail(@RequestBody EmailRequestDTO dto);
//}
