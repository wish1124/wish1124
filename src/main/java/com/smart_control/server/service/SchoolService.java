package com.smart_control.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart_control.server.model.School;
import com.smart_control.server.repository.SchoolRepository;

@Service
public class SchoolService {
    @Autowired
    private SchoolRepository schoolRepository;

    public School addSchool(String name) {
        School school = new School(name);
        return schoolRepository.save(school);
    }

    public List<School> getSchoolList() {
        return schoolRepository.findAll();
    }
}
