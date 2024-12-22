package org.example.productcatalogservice.controllers;

import org.example.productcatalogservice.dtos.CategoryDto;
import org.example.productcatalogservice.dtos.ProductDto;
import org.example.productcatalogservice.models.Category;
import org.example.productcatalogservice.models.Product;
import org.example.productcatalogservice.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

    // We can add qualifiers, which we can use with @Qualifier with @Autowired
    // Refer FakeStoreProductService and ProductController for example
    @Autowired
    @Qualifier("fks")
    IProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(from(product));
        }
        return new ResponseEntity<>(productDtos, HttpStatus.OK);
    }

    // If it is {productId} in path, then use @PathVariable("productId")
    @GetMapping("/{id}")
    //public ProductDto findProductById(@PathVariable Long id) {
    public ResponseEntity<ProductDto> findProductById(@PathVariable Long id) {
        // Example of sending Headers in the response
        // Headers are usually used to send some metadata or tokens etc. in request & response
        // These are in addition to the ones, being sent by DispatcherServlet.
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

        // By default DispatcherServlet sends HttpStatusCode in response.
        // If we want to overwrite, we can use ResponseEntity
        if (id <= 0) {
            //return null;
            headers.add("Called by", "Budwak");
            return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);
        }

        Product product = productService.getProductById(id);
        headers.add("Called by", "Intelligent");
        if (product == null) {
            //return null;
            return new ResponseEntity<>(null, headers, HttpStatus.BAD_REQUEST);
        }
        //return from(product);
        return new ResponseEntity<>(from(product), headers, HttpStatus.OK);
    }

    // Generally Post should return the object created, rather than void / bool etc.
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        Product product = productService.createProduct(from(productDto));
        if (product == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(from(product), HttpStatus.CREATED);
    }

    private ProductDto from(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setImageUrl(product.getImageUrl());

        if (product.getCategory() != null) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(product.getCategory().getId());
            categoryDto.setName(product.getCategory().getName());
            categoryDto.setDescription(product.getCategory().getDescription());
            productDto.setCategoryDto(categoryDto);
        }

        return productDto;
    }

    private Product from(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setImageUrl(productDto.getImageUrl());
        if (productDto.getCategoryDto() != null) {
            product.setCategory(from(productDto.getCategoryDto()));
        }
        return product;
    }

    private Category from(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        return category;
    }
}
