package com.smartcontrol.dto;

import com.smartcontrol.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String phone;
    private User.Role role;
    private User.Status status;
    private Long schoolId;
    private String schoolName;

    public static UserDTO toDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setSchoolId(user.getSchool() != null ? user.getSchool().getId() : null);
        dto.setSchoolName(user.getSchool() != null ? user.getSchool().getName() : null);
        return dto;
    }

    public User toEntity() {
        User user = new User();
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setName(this.name);
        user.setPhone(this.phone);
        user.setRole(this.role);
        return user;
    }
}
