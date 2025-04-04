package com.employwise.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class EmployeeDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeRequest {
        private String employeeName;
        private String phoneNumber;
        private String email;
        private String reportsTo;
        private String profileImage;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeResponse {
        private String id;
        private String employeeName;
        private String phoneNumber;
        private String email;
        private String reportsTo;
        private String profileImage;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ManagerRequest {
        private String employeeId;
        private Integer level;
    }
}