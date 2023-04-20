package com.manager.model.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Session {
    private UUID id;
    private UUID s3Folder;
    private String absolutePath;
    private User owner;
    private String title;
    private String description;
}
