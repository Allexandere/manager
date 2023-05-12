package com.manager.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SessionDto {
    private UUID ownerId;
    private String title;
    private String description;
    private String absolutePath;
}
