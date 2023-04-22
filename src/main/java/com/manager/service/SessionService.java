package com.manager.service;

import com.manager.model.dto.SessionDto;
import com.manager.model.dto.SnapshotDto;
import com.manager.model.pojo.Session;
import com.manager.model.pojo.Snapshot;

import java.util.List;
import java.util.UUID;

public interface SessionService {
    Session createSession(SessionDto sessionDto);

    Snapshot createSnapshot(SnapshotDto snapshotDto);

    List<Session> getSessions(UUID ownerId);

    List<Snapshot> getSnapshots(UUID sessionId);
}
