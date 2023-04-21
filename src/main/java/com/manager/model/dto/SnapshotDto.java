package com.manager.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class SnapshotDto {
    private UUID sessionId;
    private String title;
    private String description;
}
