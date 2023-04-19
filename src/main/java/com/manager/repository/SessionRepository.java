package com.manager.repository;

import com.manager.model.entity.SessionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SessionRepository extends CrudRepository<SessionEntity, UUID> {
}
