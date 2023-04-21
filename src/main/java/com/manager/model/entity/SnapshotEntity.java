package com.manager.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "manager_snapshot", schema = "diploma")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class SnapshotEntity extends ComparableEntity {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @CreationTimestamp
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private SessionEntity session;
}
