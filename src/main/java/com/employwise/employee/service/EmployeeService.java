package com.employwise.employee.service;

import com.employwise.employee.dto.EmployeeDto;
import com.employwise.employee.exception.ResourceNotFoundException;
import com.employwise.employee.model.Employee;
import com.employwise.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private EmailService emailService;

    // Entry Level - Add Employee
    public String addEmployee(EmployeeDto.EmployeeRequest employeeRequest) {
        // Generate a UUID for the new employee
        String employeeId = UUID.randomUUID().toString();
        
        Employee employee = new Employee(
            employeeId,
            employeeRequest.getEmployeeName(),
            employeeRequest.getPhoneNumber(),
            employeeRequest.getEmail(),
            employeeRequest.getReportsTo(),
            employeeRequest.getProfileImage()
        );
        
        Employee savedEmployee = employeeRepository.save(employee);
        
        // Advanced Level - Send email to manager if reportsTo is provided
        if (employeeRequest.getReportsTo() != null && !employeeRequest.getReportsTo().isEmpty()) {
            sendEmailToManager(savedEmployee);
        }
        
        return savedEmployee.getId();
    }
    
    // Entry Level - Get All Employees
    public List<EmployeeDto.EmployeeResponse> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return mapToEmployeeResponseList(employees);
    }
    
    // Entry Level - Delete Employee by ID
    public void deleteEmployee(String employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException("Employee not found with id: " + employeeId);
        }
        employeeRepository.deleteById(employeeId);
    }
    
    // Entry Level - Update Employee by ID
    public EmployeeDto.EmployeeResponse updateEmployee(String employeeId, EmployeeDto.EmployeeRequest employeeRequest) {
        Employee existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        
        existingEmployee.setEmployeeName(employeeRequest.getEmployeeName());
        existingEmployee.setPhoneNumber(employeeRequest.getPhoneNumber());
        existingEmployee.setEmail(employeeRequest.getEmail());
        existingEmployee.setReportsTo(employeeRequest.getReportsTo());
        existingEmployee.setProfileImage(employeeRequest.getProfileImage());
        
        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return mapToEmployeeResponse(updatedEmployee);
    }
    
    // Intermediate Level - Get nth Level Manager
    public EmployeeDto.EmployeeResponse getNthLevelManager(String employeeId, int level) {
        if (level < 1) {
            throw new IllegalArgumentException("Level must be a positive integer");
        }
        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        
        Employee manager = employee;
        int currentLevel = 0;
        
        while (currentLevel < level) {
            if (manager.getReportsTo() == null || manager.getReportsTo().isEmpty()) {
                throw new ResourceNotFoundException("Level " + level + " manager does not exist for employee with id: " + employeeId);
            }
            
            String managerId = manager.getReportsTo();
            manager = employeeRepository.findById(managerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + managerId));
            
            currentLevel++;
        }
        
        return mapToEmployeeResponse(manager);
    }
    
    // Intermediate Level - Get Employees with Pagination and Sorting
    public Page<EmployeeDto.EmployeeResponse> getEmployeesWithPagination(
            int page, int size, String sortBy, String sortDirection) {
        
        boolean descending = sortDirection.equalsIgnoreCase("desc");
        
        // Calculate skip value for pagination
        int skip = page * size;
        
        // Get paginated and sorted data from repository
        List<Employee> employees = employeeRepository.findAll(skip, size, sortBy, descending);
        
        // Convert to DTOs
        List<EmployeeDto.EmployeeResponse> dtos = mapToEmployeeResponseList(employees);
        
        // Create a PageImpl to maintain compatibility with Spring Data
        // Note: Total elements count is approximated - Ektorp doesn't provide an easy way to get total count
        long totalElements = employees.size() == size ? (page + 2) * size : (page + 1) * size;
        
        return new PageImpl<>(dtos, PageRequest.of(page, size), totalElements);
    }
    
    // Helper methods
    private EmployeeDto.EmployeeResponse mapToEmployeeResponse(Employee employee) {
        return new EmployeeDto.EmployeeResponse(
            employee.getId(),
            employee.getEmployeeName(),
            employee.getPhoneNumber(),
            employee.getEmail(),
            employee.getReportsTo(),
            employee.getProfileImage()
        );
    }
    
    private List<EmployeeDto.EmployeeResponse> mapToEmployeeResponseList(List<Employee> employees) {
        return employees.stream()
                .map(this::mapToEmployeeResponse)
                .collect(Collectors.toList());
    }
    
    // Advanced Level - Send email to manager
    private void sendEmailToManager(Employee newEmployee) {
        if (newEmployee.getReportsTo() == null || newEmployee.getReportsTo().isEmpty()) {
            return;
        }
        
        Employee manager = employeeRepository.findById(newEmployee.getReportsTo())
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + newEmployee.getReportsTo()));
        
        String subject = "New Employee Assignment Notification";
        String message = newEmployee.getEmployeeName() + " will now work under you. " +
                "Mobile number is " + newEmployee.getPhoneNumber() + " and email is " + 
                newEmployee.getEmail();
        
        emailService.sendEmail(manager.getEmail(), subject, message);
    }
}