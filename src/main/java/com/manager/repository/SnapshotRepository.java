package com.manager.repository;

import com.manager.model.entity.SnapshotEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SnapshotRepository extends CrudRepository<SnapshotEntity, UUID> {
}
