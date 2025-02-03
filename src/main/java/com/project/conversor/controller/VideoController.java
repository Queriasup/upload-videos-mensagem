package com.project.conversor.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.project.conversor.producer.VideoMessageProducer;
import com.project.conversor.services.S3Service;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {

    private final S3Service s3Service;
    private final VideoMessageProducer videoMessageProducer; 

    @PostMapping("/upload")
    public ResponseEntity<?> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("email") @Email String email,
            @RequestParam("format") @NotEmpty String format
    ) {
        try {
            // Faz upload do vídeo para o S3
            String videoUrl = s3Service.uploadFile(file);

            // Envia mensagem para RabbitMQ usando o VideoMessageProducer
            videoMessageProducer.sendMessage(email, videoUrl, format);

            return ResponseEntity.ok(Map.of(
                    "message", "Upload bem-sucedido e mensagem enviada!",
                    "videoUrl", videoUrl
            ));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erro ao fazer upload do vídeo.");
        }
    }
}
