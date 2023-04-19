package com.manager.service;

import com.manager.model.dto.SessionDto;
import com.manager.model.pojo.Session;

import java.util.UUID;

public interface SessionService {
    Session createSession(SessionDto sessionDto, UUID ownerId);
}
