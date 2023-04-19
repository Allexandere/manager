package com.manager.service.impl;

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

import javax.annotation.PostConstruct;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionServiceImpl extends ModelMapper implements SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @PostConstruct
    void excludeTemplateMapping() {
        this.createTypeMap(SessionEntity.class, Session.class)
                .addMappings(mapper -> {
                    mapper.skip(Session::setOwner);
                });
    }

    @Override
    public Session createSession(SessionDto sessionDto, UUID ownerId) {
        UserEntity owner = userRepository.findById(ownerId).orElseThrow(IllegalArgumentException::new);
        SessionEntity sessionEntity = this.map(sessionDto, SessionEntity.class);
        log.info("mapped " + sessionEntity);
        sessionEntity.setOwner(owner);
        SessionEntity savedSession = sessionRepository.save(sessionEntity);
        log.info("saved " + sessionEntity);
        Session mappedSession = this.map(savedSession, Session.class);
        mappedSession.setOwner(this.map(owner, User.class));
        return mappedSession;
    }
}
