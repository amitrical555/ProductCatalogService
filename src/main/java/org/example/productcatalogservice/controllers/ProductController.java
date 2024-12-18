package org.example.productcatalogservice.controllers;

import org.example.productcatalogservice.models.Category;
import org.example.productcatalogservice.models.Product;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

// @Controller, @Service etc. are subtypes of @Component
// @RestController is subtype of @Controller
// @RestController is best used when you want to return data
// (e.g. JSON or XML) rather than a view (HTML).
@RestController
@RequestMapping("/products")
public class ProductController {
    // @GetMapping for HttpMethod GET and similar for others
    // 'Spring by default shows 'White label error' page, when there is 404 or 500 error.

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

    // If it is {productId} in path, then use @PathVariable("productId")
    @GetMapping("/{id}")
    public Product findProductById(@PathVariable Long id) {
        Product product = new Product();
        product.setId(id);
        product.setName("Product");
        return product;
    }

    // Generally Post should return the object created, rather than void / bool etc.
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return product;
    }
}
