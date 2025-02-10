package com.project.conversor.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VideoMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    //Esse valor está definido no application.properties
    @Value("${rabbitmq.exchange}")
    private String exchange;

    //Esse valor está definido no application.properties
    @Value("${rabbitmq.routing-key}")
    private String routingKey;

    //Construtor
    public VideoMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter()); // Configura o conversor JSON
    }

    //Forma a mensagem e envia
    public void sendMessage(String email, String videoUrl, String format) {
        Map<String, String> message = new HashMap<>();
        message.put("email", email);
        message.put("videoUrl", videoUrl);
        message.put("format", format);

        // Envia a mensagem convertida para JSON
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        System.out.println("📤 Mensagem enviada para RabbitMQ: " + message);
    }
}