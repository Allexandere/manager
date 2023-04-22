package com.manager.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.manager.model.dto.SessionDto;
import com.manager.model.dto.SnapshotDto;
import com.manager.model.entity.SessionEntity;
import com.manager.model.entity.SnapshotEntity;
import com.manager.model.entity.UserEntity;
import com.manager.model.pojo.Session;
import com.manager.model.pojo.Snapshot;
import com.manager.model.pojo.User;
import com.manager.repository.SessionRepository;
import com.manager.repository.SnapshotRepository;
import com.manager.repository.UserRepository;
import com.manager.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionServiceImpl extends ModelMapper implements SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final SnapshotRepository snapshotRepository;

    private static final String BASE_BUCKET = "24211693-897d379b-b4c3-4a54-9db8-2e29aeb00169";
    private final AmazonS3 client;

    private static boolean folderAlreadyExists(SessionDto sessionDto, UserEntity owner) {
        return owner.getSessions().stream().anyMatch(session -> session.getAbsolutePath().equals(sessionDto.getAbsolutePath()));
    }

    @Override
    public Session createSession(SessionDto sessionDto) {
        UserEntity owner = getOwner(sessionDto.getOwnerId());
        if (folderAlreadyExists(sessionDto, owner)) {
            throw new IllegalArgumentException("Folder already exists");
        }
        SessionEntity savedSession = sessionRepository.save(mapToEntity(sessionDto, owner));
        createS3Folder(savedSession.getId().toString());
        return mapToSession(savedSession);
    }

    @Override
    public Snapshot createSnapshot(SnapshotDto snapshotDto) {
        SessionEntity session = getSessionEntity(snapshotDto.getSessionId());
        SnapshotEntity savedSnapshot = snapshotRepository.save(mapToEntity(snapshotDto, session));
        String relativeS3Path = session.getId() + "/" + savedSnapshot.getId();
        createS3Folder(relativeS3Path);
        return mapToSnapshot(savedSnapshot);
    }

    @Override
    public List<Session> getSessions(UUID ownerId) {
        UserEntity owner = getOwner(ownerId);
        return owner.getSessions().stream()
                .map(this::mapToSession)
                .collect(Collectors.toList());
    }

    @Override
    public List<Snapshot> getSnapshots(UUID sessionId) {
        SessionEntity session = getSessionEntity(sessionId);
        return session.getSnapshots().stream()
                .map(this::mapToSnapshot)
                .collect(Collectors.toList());
    }

    @Override
    public Session getSession(UUID sessionId) {
        return mapToSession(getSessionEntity(sessionId));
    }

    private Snapshot mapToSnapshot(SnapshotEntity snapshotEntity) {
        Snapshot snapshot = this.map(snapshotEntity, Snapshot.class);
        String relativeS3Path = snapshotEntity.getSession().getId() + "/" + snapshotEntity.getId();
        snapshot.setRelativeS3Path(relativeS3Path);
        return snapshot;
    }

    private SnapshotEntity mapToEntity(SnapshotDto snapshotDto, SessionEntity session) {
        SnapshotEntity mappedSnapshot = this.map(snapshotDto, SnapshotEntity.class);
        mappedSnapshot.setSession(session);
        return mappedSnapshot;
    }

    private SessionEntity getSessionEntity(UUID sessionId) {
        return sessionRepository.findById(sessionId).orElseThrow(IllegalArgumentException::new);
    }

    private SessionEntity mapToEntity(SessionDto sessionDto, UserEntity owner) {
        SessionEntity mappedSession = this.map(sessionDto, SessionEntity.class);
        mappedSession.setOwner(owner);
        return mappedSession;
    }

    private Session mapToSession(SessionEntity sessionEntity) {
        Session mappedSession = this.map(sessionEntity, Session.class);
        mappedSession.setOwner(new User(sessionEntity.getOwner().getId()));
        return mappedSession;
    }

    private UserEntity getOwner(UUID ownerId) {
        return userRepository.findById(ownerId).orElseThrow(IllegalArgumentException::new);
    }

    private void createS3Folder(String path) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);

        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

        PutObjectRequest putObjectRequest = new PutObjectRequest(BASE_BUCKET,
                path + "/",
                emptyContent,
                metadata);

        client.putObject(putObjectRequest);
    }
}
