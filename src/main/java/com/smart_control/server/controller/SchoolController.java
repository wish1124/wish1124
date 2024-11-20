package com.smart_control.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smart_control.server.model.School;
import com.smart_control.server.service.SchoolService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

// 1. add school
// 2. fetch schools

@RestController
@RequestMapping("/api/school")
public class SchoolController {
    @Autowired
    SchoolService schoolService;

    @PostMapping("/")
    public ResponseEntity<School> addSchool(@RequestParam String name) {
        return ResponseEntity.ok(schoolService.addSchool(name));
    }
    
    @GetMapping("/")
    public ResponseEntity<List<School>> getSchoolList() {
        return ResponseEntity.ok(schoolService.getSchoolList());
    }
    
}
