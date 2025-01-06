package com.smartcontrol.dto;

import com.smartcontrol.model.School;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchoolDTO {
    private Long id;
    private String name;
    private String address;
    private Long managerId; 
    private boolean isActive;

    
    public static SchoolDTO toDto(School school) {
        SchoolDTO dto = new SchoolDTO();
        dto.setId(school.getId());
        dto.setName(school.getName());
        dto.setAddress(school.getAddress());
        dto.setManagerId(school.getManager() != null ? school.getManager().getId() : null);
        dto.setActive(school.isActive());
        return dto;
    }
}
