package org.example.productcatalogservice.controllers;

import org.example.productcatalogservice.models.Category;
import org.example.productcatalogservice.models.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @GetMapping
    public List<Product> getAllProducts() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setDescription("Product 1 Description");
        product1.setPrice(100.0);
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Category 1");
        category1.setDescription("Category 1 Description");
        product1.setCategory(category1);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setDescription("Product 2 Description");
        product2.setPrice(200.0);
        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Category 2");
        category2.setDescription("Category 2 Description");
        product2.setCategory(category2);

        List<Product> products = new ArrayList<Product>();
        products.add(product1);
        products.add(product2);
        return products;
    }

    @GetMapping("/{id}")
    public Product findProductById(@PathVariable Long id) {
        Product product = new Product();
        product.setId(id);
        product.setName("Product");
        return product;
    }
}
