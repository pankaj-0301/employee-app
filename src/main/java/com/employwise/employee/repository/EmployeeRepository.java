package com.employwise.employee.repository;

import com.employwise.employee.model.Employee;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepository extends CouchDbRepositorySupport<Employee> {
    
    public EmployeeRepository(CouchDbConnector db) {
        super(Employee.class, db);
        initStandardDesignDocument();
    }
    
    @GenerateView
    public List<Employee> findByReportsTo(String managerId) {
        return queryView("by_reportsTo", managerId);
    }
    
    // Find all employees (equivalent to findAll in Spring Data)
    public List<Employee> findAll() {
        return getAll();
    }
    
    // Find by ID with Optional (equivalent to findById in Spring Data)
    public Optional<Employee> findById(String id) {
        try {
            Employee employee = get(id);
            return Optional.ofNullable(employee);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    // Check if entity exists (equivalent to existsById in Spring Data)
    public boolean existsById(String id) {
        return contains(id);
    }
    
    // Save an entity (equivalent to save in Spring Data)
    public Employee save(Employee employee) {
        if (employee.getId() == null || !contains(employee.getId())) {
            add(employee);
        } else {
            update(employee);
        }
        return employee;
    }
    
    // Delete by ID (equivalent to deleteById in Spring Data)
    public void deleteById(String id) {
        Employee employee = get(id);
        if (employee != null) {
            remove(employee);
        }
    }
    
    // Additional method for pagination (simulated as Ektorp doesn't have direct pagination support)
    public List<Employee> findAll(int skip, int limit, String sortField, boolean descending) {
        ViewQuery query = createQuery("all")
                .descending(descending)
                .skip(skip)
                .limit(limit);
        
        // Sort field needs a view to be created for it
        // This is a simplification - in a real app, you'd need to create views for each sort field
        
        return db.queryView(query, Employee.class);
    }
}