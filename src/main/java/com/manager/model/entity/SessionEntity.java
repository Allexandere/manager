package com.manager.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "manager_session", schema = "diploma")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class SessionEntity extends ComparableEntity {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "absolute_path")
    private String absolutePath;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @GeneratedValue
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "s3_folder")
    private UUID s3Folder;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;
}
