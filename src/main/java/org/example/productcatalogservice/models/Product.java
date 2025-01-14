package org.example.productcatalogservice.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity // To create table for it. Need a Primary Key though.
public class Product extends BaseModel {
    private String name;
    private String description;
    private String imageUrl;
    private Double price;

    // Shows red, so need to mention or create a relationship.
    // Relation is defined as <Class> to <Field>
    @ManyToOne(cascade = CascadeType.ALL)
    // Many Products can be associated with One Category
    // As we want category to automatically created / modified / deleted
    // based on operation on Product so we used CascadeType.ALL
    private Category category;
    private Boolean isPrime;
}
