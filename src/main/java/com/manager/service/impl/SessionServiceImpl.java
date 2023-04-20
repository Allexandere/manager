package com.manager.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.manager.model.dto.SessionDto;
import com.manager.model.entity.SessionEntity;
import com.manager.model.entity.UserEntity;
import com.manager.model.pojo.Session;
import com.manager.model.pojo.User;
import com.manager.repository.SessionRepository;
import com.manager.repository.UserRepository;
import com.manager.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionServiceImpl extends ModelMapper implements SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    private static final String BASE_BUCKET = "24211693-897d379b-b4c3-4a54-9db8-2e29aeb00169";
    private final AmazonS3 client;

    private static boolean folderAlreadyExists(SessionDto sessionDto, UserEntity owner) {
        return owner.getSessions().stream().anyMatch(session -> session.getAbsolutePath().equals(sessionDto.getAbsolutePath()));
    }

    @Override
    public Session createSession(SessionDto sessionDto, UUID ownerId) {
        UserEntity owner = userRepository.findById(ownerId).orElseThrow(IllegalArgumentException::new);
        if (folderAlreadyExists(sessionDto, owner)) {
            throw new IllegalArgumentException("Folder already exists");
        }
        SessionEntity sessionEntity = this.map(sessionDto, SessionEntity.class);
        sessionEntity.setOwner(owner);
        SessionEntity savedSession = sessionRepository.save(sessionEntity);
        createS3Folder(savedSession);
        Session mappedSession = this.map(savedSession, Session.class);
        mappedSession.setOwner(new User(owner.getId()));
        return mappedSession;
    }

    private void createS3Folder(SessionEntity savedSession) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);

        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

        PutObjectRequest putObjectRequest = new PutObjectRequest(BASE_BUCKET,
                savedSession.getS3Folder() + "/",
                emptyContent,
                metadata);

        client.putObject(putObjectRequest);
    }
}
