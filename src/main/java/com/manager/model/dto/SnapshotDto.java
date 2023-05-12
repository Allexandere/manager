package com.manager.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SnapshotDto {
    private UUID sessionId;
    private String title;
    private String description;
}
