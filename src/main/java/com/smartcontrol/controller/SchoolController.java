package com.smartcontrol.controller;

import com.smartcontrol.dto.SchoolDTO;
import com.smartcontrol.model.School;
import com.smartcontrol.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/schools")
public class SchoolController {

    @Autowired
    private SchoolService schoolService;

    
    @PostMapping(produces = "application/json; charset=utf8")
    @PreAuthorize("hasRole('ADMIN')") 
    public ResponseEntity<SchoolDTO> createSchool(@RequestBody SchoolDTO schoolDTO) {
        School school = new School();
        school.setName(schoolDTO.getName());
        school.setAddress(schoolDTO.getAddress());
        school.setActive(true);

        School createdSchool = schoolService.createSchool(school);
        return ResponseEntity.ok(SchoolDTO.toDto(createdSchool));
    }

    
    @GetMapping
    public ResponseEntity<List<SchoolDTO>> getAllSchools() {
        List<School> schools = schoolService.getAllSchools();
        List<SchoolDTO> response = schools.stream().map(SchoolDTO::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<SchoolDTO> getSchoolById(@PathVariable Long id) {
        School school = schoolService.getSchoolById(id)
                .orElseThrow(() -> new IllegalArgumentException("학교를 찾을 수 없습니다."));
        return ResponseEntity.ok(SchoolDTO.toDto(school));
    }

    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") 
    public ResponseEntity<SchoolDTO> updateSchool(@PathVariable Long id, @RequestBody SchoolDTO schoolDTO) {
        School updatedSchool = new School();
        updatedSchool.setName(schoolDTO.getName());
        updatedSchool.setAddress(schoolDTO.getAddress());
        updatedSchool.setActive(schoolDTO.isActive());

        School savedSchool = schoolService.updateSchool(id, updatedSchool);
        return ResponseEntity.ok(SchoolDTO.toDto(savedSchool));
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") 
    public ResponseEntity<Void> deleteSchool(@PathVariable Long id) {
        schoolService.deleteSchool(id);
        return ResponseEntity.noContent().build();
    }

    
    @PutMapping("/{schoolId}/manager/{managerId}")
    @PreAuthorize("hasRole('ADMIN')") 
    public ResponseEntity<SchoolDTO> assignManager(@PathVariable Long schoolId, @PathVariable Long managerId) {
        School school = schoolService.assignManager(schoolId, managerId);
        return ResponseEntity.ok(SchoolDTO.toDto(school));
    }
}
