package com.employwise.employee.controller;

import com.employwise.employee.dto.EmployeeDto;
import com.employwise.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // Entry Level - Add Employee
    @PostMapping
    public ResponseEntity<Map<String, String>> addEmployee(@Valid @RequestBody EmployeeDto.EmployeeRequest request) {
        String employeeId = employeeService.addEmployee(request);
        
        Map<String, String> response = new HashMap<>();
        response.put("id", employeeId);
        response.put("message", "Employee added successfully");
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    // Entry Level - Get All Employees
    @GetMapping
    public ResponseEntity<List<EmployeeDto.EmployeeResponse>> getAllEmployees() {
        List<EmployeeDto.EmployeeResponse> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }
    
    // Entry Level - Delete Employee by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEmployee(@PathVariable String id) {
        employeeService.deleteEmployee(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Employee deleted successfully");
        
        return ResponseEntity.ok(response);
    }
    
    // Entry Level - Update Employee by ID
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto.EmployeeResponse> updateEmployee(
            @PathVariable String id, 
            @Valid @RequestBody EmployeeDto.EmployeeRequest request) {
        EmployeeDto.EmployeeResponse updatedEmployee = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(updatedEmployee);
    }
    
    // Intermediate Level - Get nth Level Manager
    @PostMapping("/manager")
    public ResponseEntity<EmployeeDto.EmployeeResponse> getNthLevelManager(
            @Valid @RequestBody EmployeeDto.ManagerRequest request) {
        EmployeeDto.EmployeeResponse manager = 
                employeeService.getNthLevelManager(request.getEmployeeId(), request.getLevel());
        return ResponseEntity.ok(manager);
    }
    
    // Intermediate Level - Get Employees with Pagination and Sorting
    @GetMapping("/paginated")
    public ResponseEntity<Page<EmployeeDto.EmployeeResponse>> getEmployeesWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "employeeName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        Page<EmployeeDto.EmployeeResponse> employees = 
                employeeService.getEmployeesWithPagination(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(employees);
    }
}