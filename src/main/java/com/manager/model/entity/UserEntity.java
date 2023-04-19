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
@Table(name = "manager_user", schema = "diploma")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserEntity extends ComparableEntity {
    @Id
    @GeneratedValue
    private UUID id;
    @OneToMany(mappedBy = "owner")
    @ToString.Exclude
    private Set<SessionEntity> sessions = new HashSet<>();
}
