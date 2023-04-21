package com.manager.model.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Snapshot {
    private UUID id;
    private String title;
    private String description;
    private String relativeS3Path;
    private LocalDateTime creationDate;
}
