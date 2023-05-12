package com.manager.integration;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.manager.S3TestClientConfig;
import com.manager.exception.throwables.FolderAlreadyExistsException;
import com.manager.exception.throwables.OwnerNotFoundException;
import com.manager.exception.throwables.SessionNotFoundException;
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
import com.manager.service.SessionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(S3TestClientConfig.class)
public class IntegrationTest {
    private static final UUID BASE_USER_ID = UUID.fromString("51be55eb-4639-4d1e-95ac-bcefeb4ee246");
    @Autowired
    private SessionService sessionService;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private SnapshotRepository snapshotRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AmazonS3 client;
    @Value("${s3.base-bucket}")
    private String BASE_BUCKET;

    @AfterEach
    void clearDatabase() {
        sessionRepository.deleteAll();
    }

    @Test
    void happyCreateSessionTest() {
        String absolutePath = "some path";
        Session session = sessionService.createSession(SessionDto.builder()
                .ownerId(BASE_USER_ID)
                .absolutePath(absolutePath)
                .build());

        assertTrue(doesFolderExists(session.getId().toString()));

        UserEntity owner = userRepository.findById(BASE_USER_ID).get();
        SessionEntity sessionEntity = sessionRepository.findById(session.getId()).get();

        assertEquals(absolutePath, session.getAbsolutePath());
        assertEquals(BASE_USER_ID, session.getOwner().getId());
        assertTrue(owner.getSessions().contains(sessionEntity));
    }

    @Test
    void ownerNotFoundCreateSessionTest() {
        OwnerNotFoundException error = assertThrows(OwnerNotFoundException.class,
                () -> sessionService.createSession(SessionDto.builder()
                        .ownerId(UUID.fromString("51be55eb-4639-4d1e-95ac-bcefeb4ee333"))
                        .absolutePath("some path")
                        .build())
        );
        assertEquals("Owner with specified ID does not exist.", error.getMessage());
        assertFalse(anyFolderExist());
    }

    @Test
    void folderAlreadyExistsCreateSessionTest() {
        sessionService.createSession(
                SessionDto.builder()
                        .ownerId(BASE_USER_ID)
                        .absolutePath("path")
                        .build()
        );
        FolderAlreadyExistsException error = assertThrows(FolderAlreadyExistsException.class,
                () -> sessionService.createSession(SessionDto.builder()
                        .ownerId(BASE_USER_ID)
                        .absolutePath("path")
                        .build())
        );
        assertEquals("Folder already exists.", error.getMessage());
        assertFalse(anyFolderExist());
    }

    @Test
    void happyCreateSnapshotTest() {
        UUID sessionId = sessionService.createSession(SessionDto.builder()
                .ownerId(BASE_USER_ID)
                .absolutePath("some path")
                .build()).getId();

        assertTrue(doesFolderExists(sessionId.toString()));

        Snapshot snapshot = sessionService.createSnapshot(SnapshotDto.builder()
                .sessionId(sessionId)
                .build());

        assertTrue(doesFolderExists(snapshot.getId().toString()));

        SnapshotEntity snapshotEntity = snapshotRepository.findById(snapshot.getId()).get();
        SessionEntity sessionEntity = sessionRepository.findById(sessionId).get();

        assertEquals(snapshot.getRelativeS3Path(), sessionId + "/" + snapshot.getId() + "/");
        assertTrue(sessionEntity.getSnapshots().contains(snapshotEntity));
        assertEquals(sessionEntity, snapshotEntity.getSession());
        assertNotNull(snapshot.getCreationDate());
    }

    @Test
    void sessionNofFoundCreateSnapshotTest() {
        SessionNotFoundException error = assertThrows(SessionNotFoundException.class,
                () ->
                        sessionService.createSnapshot(SnapshotDto.builder()
                                .sessionId(UUID.randomUUID())
                                .build())
        );
        assertEquals("Session with specified ID does not exist.", error.getMessage());
        assertFalse(anyFolderExist());
    }

    private boolean doesFolderExists(String folder) {
        return client.listObjects(BASE_BUCKET)
                .getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .anyMatch(key -> key.endsWith(folder + "/"));
    }

    private boolean anyFolderExist() {
        return client.listObjects(BASE_BUCKET).getObjectSummaries().size() == 0;
    }
}
