package com.e451.rest.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by j747951 on 6/13/2017.
 */
@CrossOrigin
@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping()
    public ResponseEntity healthCheck() {
        return ResponseEntity.ok().build();
    }
}
