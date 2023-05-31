package com.manager.service;

import com.manager.exception.throwables.FolderAlreadyExistsException;
import com.manager.exception.throwables.OwnerNotFoundException;
import com.manager.exception.throwables.SessionNotFoundException;
import com.manager.exception.throwables.SnapshotNotFoundException;
import com.manager.model.dto.SessionDto;
import com.manager.model.dto.SnapshotDto;
import com.manager.model.entity.SessionEntity;
import com.manager.model.entity.SnapshotEntity;
import com.manager.model.entity.UserEntity;
import com.manager.model.pojo.Session;
import com.manager.model.pojo.Snapshot;
import com.manager.repository.SessionRepository;
import com.manager.repository.SnapshotRepository;
import com.manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final SnapshotRepository snapshotRepository;
    private final S3Adapter s3Adapter;
    private final UniMapper uniMapper;

    @SneakyThrows
    public Session createSession(SessionDto sessionDto) {
        UserEntity owner = getOwner(sessionDto.getOwnerId());
        if (folderAlreadyExists(sessionDto, owner)) {
            throw new FolderAlreadyExistsException("Folder already exists.");
        }
        SessionEntity savedSession = sessionRepository.save(uniMapper.mapToEntity(sessionDto, owner));
        s3Adapter.createS3Folder(savedSession.getId().toString());
        return uniMapper.mapToSession(savedSession);
    }

    public Snapshot createSnapshot(SnapshotDto snapshotDto) {
        SessionEntity session = getSessionEntity(snapshotDto.getSessionId());
        SnapshotEntity savedSnapshot = snapshotRepository.save(uniMapper.mapToEntity(snapshotDto, session));
        String relativeS3Path = session.getId() + "/" + savedSnapshot.getId();
        s3Adapter.createS3Folder(relativeS3Path);
        return uniMapper.mapToSnapshot(savedSnapshot);
    }

    public List<Session> getSessions(UUID ownerId) {
        UserEntity owner = getOwner(ownerId);
        return owner.getSessions().stream()
                .map(uniMapper::mapToSession)
                .collect(Collectors.toList());
    }

    public List<Snapshot> getSnapshots(UUID sessionId) {
        SessionEntity session = getSessionEntity(sessionId);
        return session.getSnapshots().stream()
                .map(uniMapper::mapToSnapshot)
                .collect(Collectors.toList());
    }

    public Session getSession(UUID sessionId) {
        return uniMapper.mapToSession(getSessionEntity(sessionId));
    }

    public Snapshot getSnapshot(UUID snapshotId) {
        return uniMapper.mapToSnapshot(getSnapshotEntity(snapshotId));
    }

    private boolean folderAlreadyExists(SessionDto sessionDto, UserEntity owner) {
        return owner.getSessions().stream()
                .anyMatch(session -> session.getAbsolutePath().equals(sessionDto.getAbsolutePath()));
    }

    @SneakyThrows
    private SessionEntity getSessionEntity(UUID sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Session with specified ID does not exist."));
    }

    @SneakyThrows
    private SnapshotEntity getSnapshotEntity(UUID snapshotId) {
        return snapshotRepository.findById(snapshotId)
                .orElseThrow(() -> new SnapshotNotFoundException("Snapshot with specified ID does not exist."));
    }

    @SneakyThrows
    private UserEntity getOwner(UUID ownerId) {
        return userRepository.findById(ownerId)
                .orElseThrow(() -> new OwnerNotFoundException("Owner with specified ID does not exist."));
    }
}
