package com.manager.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
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
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;
    @OneToMany(mappedBy = "session", fetch = FetchType.EAGER, orphanRemoval = true)
    @ToString.Exclude
    private Set<SnapshotEntity> snapshots = new HashSet<>();
}
