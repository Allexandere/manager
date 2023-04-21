package com.manager.controller;


import com.manager.model.dto.SessionDto;
import com.manager.model.dto.SnapshotDto;
import com.manager.model.pojo.Session;
import com.manager.model.pojo.Snapshot;
import com.manager.service.SessionService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final SessionService sessionService;

    @PostMapping(path = "/session")
    @ApiOperation("Create session")
    public Session createSession(@RequestBody SessionDto sessionDto, @RequestParam(name = "ownerId") UUID ownerId) {
        log.info("MainController createSession got: " + sessionDto);
        return sessionService.createSession(sessionDto, ownerId);
    }

    @PostMapping(path = "/snapshot")
    @ApiOperation("Create snapshot")
    public Snapshot createSnapshot(@RequestBody SnapshotDto snapshotDto) {
        log.info("MainController createSnapshot got: " + snapshotDto);
        return sessionService.createSnapshot(snapshotDto);
    }

}
