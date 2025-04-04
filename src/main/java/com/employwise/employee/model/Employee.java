package com.employwise.employee.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import org.ektorp.support.CouchDbDocument;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Employee extends CouchDbDocument {
    
    // CouchDbDocument already has _id and _rev fields
    
    @JsonProperty("employeeName")
    private String employeeName;
    
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("reportsTo")
    private String reportsTo; // ID of the manager
    
    @JsonProperty("profileImage")
    private String profileImage; // URL to profile image
    
    // Constructor without ID for creating new employees
    public Employee(String employeeName, String phoneNumber, String email, String reportsTo, String profileImage) {
        this.employeeName = employeeName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.reportsTo = reportsTo;
        this.profileImage = profileImage;
    }
    
    // Constructor with ID for mapping existing employees
    public Employee(String id, String employeeName, String phoneNumber, String email, String reportsTo, String profileImage) {
        this.setId(id); // Sets the _id field in CouchDbDocument
        this.employeeName = employeeName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.reportsTo = reportsTo;
        this.profileImage = profileImage;
    }
}