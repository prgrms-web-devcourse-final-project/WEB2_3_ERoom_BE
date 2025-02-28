package com.example.eroom.domain.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OpenAiRequest {
    private String model;
    private List<Message> messages;
    private double temperature;
}

