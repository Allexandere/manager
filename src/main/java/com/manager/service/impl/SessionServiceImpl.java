package com.manager.service.impl;

import com.manager.model.dto.SessionDto;
import com.manager.model.entity.SessionEntity;
import com.manager.model.entity.UserEntity;
import com.manager.model.pojo.Session;
import com.manager.repository.SessionRepository;
import com.manager.repository.UserRepository;
import com.manager.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl extends ModelMapper implements SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Override
    public Session createSession(SessionDto sessionDto, UUID ownerId) {
        UserEntity owner = userRepository.findById(ownerId).orElseThrow(IllegalArgumentException::new);
        SessionEntity sessionEntity = this.map(sessionDto, SessionEntity.class);
        sessionEntity.setOwner(owner);
        SessionEntity savedSession = sessionRepository.save(sessionEntity);
        return this.map(savedSession, Session.class);
    }
}
