package com.fisc.declsituation.web.rest;

import com.fisc.declsituation.service.DeclsituationKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/declsituation-kafka")
public class DeclsituationKafkaResource {

    private final Logger log = LoggerFactory.getLogger(DeclsituationKafkaResource.class);

    private DeclsituationKafkaProducer kafkaProducer;

    public DeclsituationKafkaResource(DeclsituationKafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        log.debug("REST request to send to Kafka topic the message : {}", message);
        this.kafkaProducer.send(message);
    }
}
