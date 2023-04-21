package com.manager.service;

import com.manager.model.dto.SessionDto;
import com.manager.model.dto.SnapshotDto;
import com.manager.model.pojo.Session;
import com.manager.model.pojo.Snapshot;

import java.util.UUID;

public interface SessionService {
    Session createSession(SessionDto sessionDto, UUID ownerId);

    Snapshot createSnapshot(SnapshotDto snapshotDto);
}
