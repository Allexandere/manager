package com.manager.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class SessionDto {
    private UUID ownerId;
    private String title;
    private String description;
    private String absolutePath;
}
