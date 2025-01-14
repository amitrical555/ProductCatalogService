package org.example.productcatalogservice.models;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter

// We don't want to create a table for this class,
// but tables for all child classes should contain these columns.
@MappedSuperclass
public abstract class BaseModel {
    @Id // Indicates this field as Primary Key in the table
    private Long id;
    private Date createdAt;
    private Date lastUpdatedAt;
    // Data is actually never deleted, ony state is updated to filter
    private State state;
}
