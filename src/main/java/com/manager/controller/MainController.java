package com.manager.controller;


import com.manager.model.dto.SessionDto;
import com.manager.model.dto.SnapshotDto;
import com.manager.model.pojo.Session;
import com.manager.model.pojo.Snapshot;
import com.manager.service.SessionService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final SessionService sessionService;

    @PostMapping(path = "/session")
    @ApiOperation("Create session")
    public Session createSession(@RequestBody SessionDto sessionDto) {
        log.info("MainController createSession got: " + sessionDto);
        return sessionService.createSession(sessionDto);
    }

    @PostMapping(path = "/snapshot")
    @ApiOperation("Create snapshot")
    public Snapshot createSnapshot(@RequestBody SnapshotDto snapshotDto) {
        log.info("MainController createSnapshot got: " + snapshotDto);
        return sessionService.createSnapshot(snapshotDto);
    }

    @GetMapping(path = "/session")
    @ApiOperation("Get all sessions of the owner")
    public List<Session> getSessions(@RequestParam(value = "ownerId") UUID ownerId) {
        return sessionService.getSessions(ownerId);
    }

    @GetMapping(path = "/session")
    @ApiOperation("Get specific session")
    public Session getSession(@RequestParam(value = "sessionId") UUID sessionId) {
        return sessionService.getSession(sessionId);
    }

    @GetMapping(path = "/snapshot")
    @ApiOperation("Get all snapshots of the session")
    public List<Snapshot> getSnapshots(@RequestParam(value = "sessionId") UUID sessionId) {
        return sessionService.getSnapshots(sessionId);
    }

}
