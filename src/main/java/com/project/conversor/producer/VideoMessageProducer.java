package com.project.conversor.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

    //Cria um template do rabbit
    public VideoMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    //Forma a mensagem e envia 
    public void sendMessage(String email, String videoUrl, String format) {
        Map<String, String> message = new HashMap<>();
        message.put("email", email);
        message.put("videoUrl", videoUrl);
        message.put("format", format);

        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        //Se funcionar printa no console a mensagem enviada
        System.out.println("📤 Mensagem enviada para RabbitMQ: " + message);
    }
}