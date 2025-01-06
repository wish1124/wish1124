package com.smartcontrol.service;

import com.smartcontrol.model.Device;
import com.smartcontrol.model.School;
import com.smartcontrol.model.User;
import com.smartcontrol.repository.DeviceRepository;
import com.smartcontrol.repository.SchoolRepository;
import com.smartcontrol.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchoolService {

    @Autowired
    public SchoolRepository schoolRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public DeviceRepository deviceRepository;

    
    public School createSchool(School school) {
        if (schoolRepository.findByName(school.getName()).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 학교입니다.");
        }
        return schoolRepository.save(school);
    }

    
    public List<School> getAllSchools() {
        return schoolRepository.findAll();
    }

    
    public Optional<School> getSchoolById(Long id) {
        return schoolRepository.findById(id);
    }

    
    public School updateSchool(Long id, School updatedSchool) {
        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("학교를 찾을 수 없습니다."));
        school.setName(updatedSchool.getName());
        school.setAddress(updatedSchool.getAddress());
        school.setActive(updatedSchool.isActive());
        return schoolRepository.save(school);
    }

    
    public School assignManager(Long schoolId, Long managerId) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("학교를 찾을 수 없습니다."));
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("매니저를 찾을 수 없습니다."));

        if (!manager.getRole().equals(User.Role.MANAGER)) {
            throw new IllegalArgumentException("해당 사용자는 매니저 권한이 없습니다.");
        }

        school.setManager(manager);
        return schoolRepository.save(school);
    }

    
    public void deleteSchool(Long schoolId) {
        
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("학교를 찾을 수 없습니다."));

        List<Device> devices = deviceRepository.findBySchool(school);
        deviceRepository.deleteAll(devices);

        List<User> users = userRepository.findBySchoolId(schoolId);
        userRepository.deleteAll(users);

        schoolRepository.delete(school);
    }
}
