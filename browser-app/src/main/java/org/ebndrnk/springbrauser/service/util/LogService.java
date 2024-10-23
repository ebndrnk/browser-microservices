package org.ebndrnk.springbrauser.service.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "springboot_logs";



    public void logInfo(String message) {
        logMessage("INFO", message);
    }

    public void logWarn(String message) {
        logMessage("WARN", message);
    }

    public void logError(String message) {
        logMessage("ERROR", message);
    }

    public void logDebug(String message) {
        logMessage("DEBUG", message);
    }

    private void logMessage(String level, String message) {
        switch (level) {
            case "INFO":
                log.info(message);
                break;
            case "WARN":
                log.warn(message);
                break;
            case "ERROR":
                log.error(message);
                break;
        }


        CompletableFuture.runAsync(() -> {
            try {
                kafkaTemplate.send(TOPIC, message).get();
                log.info("Message sent to Kafka: {}", message);
            } catch (Exception e) {
                log.error("Failed to send message to Kafka: {}", e.getMessage());
            }
        });
    }

}
