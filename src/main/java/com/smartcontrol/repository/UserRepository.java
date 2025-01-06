package com.smartcontrol.repository;

import com.smartcontrol.model.School;
import com.smartcontrol.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); 

    List<User> findByStatus(User.Status status); 

    List<User> findBySchoolId(Long schoolId); 

    Optional<User> findByRoleAndSchool(User.Role role, School school);
}
