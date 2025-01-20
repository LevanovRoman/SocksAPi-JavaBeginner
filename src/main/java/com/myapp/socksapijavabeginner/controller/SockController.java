package com.myapp.socksapijavabeginner.controller;

import com.myapp.socksapijavabeginner.dto.SockDto;
import com.myapp.socksapijavabeginner.service.SockService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/socks")
public class SockController {

    @Resource
    private SockService sockService;

    @PostMapping(value = "/income", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> income(@NotNull @RequestBody SockDto sockDto){
        String response = sockService.arrivalSock(sockDto);
        return ResponseEntity.ok(response);
    }
    @PostMapping(value = "/outcome", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> outcome(@NotNull @RequestBody SockDto sockDto){
        String response = sockService.departureSock(sockDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateSock(@NotNull @PathVariable int id, @NotNull @RequestBody SockDto sockDto) {
        String response = sockService.updateSock(id, sockDto);
        return ResponseEntity.ok(response);
    }
}
