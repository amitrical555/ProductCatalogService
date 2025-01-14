package org.example.productcatalogservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

@Entity // To create table for this. Should have a Primary Key.
public class Category extends BaseModel {
    private String name;
    private String description;

    @OneToMany(mappedBy = "category")
    // mappedBy = <fieldname in another class for relation>, tells JPA to not
    // consider this twice, we have already considered this relationship
    // using mentioned field in another class, So ignore this in DB.
    private List<Product> products; // We added it only for access pattern, otherwise could delete.

}
