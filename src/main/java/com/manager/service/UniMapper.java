package com.manager.service;

import com.manager.model.dto.SessionDto;
import com.manager.model.dto.SnapshotDto;
import com.manager.model.entity.SessionEntity;
import com.manager.model.entity.SnapshotEntity;
import com.manager.model.entity.UserEntity;
import com.manager.model.pojo.Session;
import com.manager.model.pojo.Snapshot;
import com.manager.model.pojo.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UniMapper extends ModelMapper {

    public Snapshot mapToSnapshot(SnapshotEntity snapshotEntity) {
        Snapshot snapshot = this.map(snapshotEntity, Snapshot.class);
        String relativeS3Path = snapshotEntity.getSession().getId() + "/" + snapshotEntity.getId() + "/";
        snapshot.setRelativeS3Path(relativeS3Path);
        return snapshot;
    }

    public SnapshotEntity mapToEntity(SnapshotDto snapshotDto, SessionEntity session) {
        SnapshotEntity mappedSnapshot = this.map(snapshotDto, SnapshotEntity.class);
        mappedSnapshot.setSession(session);
        return mappedSnapshot;
    }

    public SessionEntity mapToEntity(SessionDto sessionDto, UserEntity owner) {
        SessionEntity mappedSession = this.map(sessionDto, SessionEntity.class);
        mappedSession.setOwner(owner);
        return mappedSession;
    }

    public Session mapToSession(SessionEntity sessionEntity) {
        Session mappedSession = this.map(sessionEntity, Session.class);
        mappedSession.setOwner(new User(sessionEntity.getOwner().getId()));
        return mappedSession;
    }

}
