package org.example.productcatalogservice.repositories;

import org.example.productcatalogservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// JpaRepository<[Name of Entity class], [DataType of Primary Key for this Entity]>
public interface ProductRepo extends JpaRepository<Product, Long> {
}