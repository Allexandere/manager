package com.manager.controller;


import com.manager.model.dto.SessionDto;
import com.manager.model.pojo.Session;
import com.manager.service.SessionService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/manager")
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final SessionService sessionService;

    @PostMapping(path = "/{ownerId}/create-session")
    @ApiOperation("Create session")
    public Session createSession(@RequestBody SessionDto sessionDto, @PathVariable(name = "ownerId") UUID ownerId) {
        return sessionService.createSession(sessionDto, ownerId);
    }

}
