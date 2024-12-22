package org.example.productcatalogservice.services;

import org.example.productcatalogservice.dtos.FakeStoreProductDto;
import org.example.productcatalogservice.models.Category;
import org.example.productcatalogservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// We can add qualifiers, which we can use with @Qualifier with @Autowired
// Refer FakeStoreProductService and ProductController for example
@Service("fks")
public class FakeStoreProductService implements IProductService {
    // @Autowired inject the singleton object of the specified dependency
    // Earlier we used to create a parameterized constructor for it.
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    public Product getProductById(Long id) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        // restTemplate.getForObject directly returns the object,
        // where as restTemplate.getForEntity returns a response
        // with additional details, e.g HttpStatusCode, ResponseBody. Headers etc.
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity =
                restTemplate.getForEntity(
                        "https://fakestoreapi.com/products/{id}",
                        FakeStoreProductDto.class,
                        id);
        if (fakeStoreProductDtoResponseEntity.getStatusCode().equals(HttpStatus.OK)
            && fakeStoreProductDtoResponseEntity.getBody() != null) {
            return from(fakeStoreProductDtoResponseEntity.getBody());
        }
        return null;
    }

    public List<Product> getAllProducts() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDto[]> fakeStoreProductListEntity =
                restTemplate.getForEntity(
                        "https://fakestoreapi.com/products",
                        FakeStoreProductDto[].class);
        if (fakeStoreProductListEntity.getStatusCode().equals(HttpStatus.OK)
            && fakeStoreProductListEntity.getBody() != null) {
            List<Product> productList = new ArrayList<>();
            for (FakeStoreProductDto fakeStoreProductDto : fakeStoreProductListEntity.getBody()) {
                productList.add(from(fakeStoreProductDto));
            }
            return productList;
        }
        return null;
    }

    public Product createProduct(Product product) {
        FakeStoreProductDto fakeStoreProductDto = from(product);
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDto> responseEntity =
                restTemplate.postForEntity(
                        "https://fakestoreapi.com/products",
                        fakeStoreProductDto,
                        FakeStoreProductDto.class
                );
        if ((responseEntity.getStatusCode().equals(HttpStatus.OK)
                || responseEntity.getStatusCode().equals(HttpStatus.CREATED))
            && responseEntity.getBody() != null) {
            return from(responseEntity.getBody());
        }
        return null;
    }

    private FakeStoreProductDto from(Product product) {
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
        fakeStoreProductDto.setId(product.getId());
        fakeStoreProductDto.setTitle(product.getName());
        fakeStoreProductDto.setDescription(product.getDescription());
        fakeStoreProductDto.setPrice(product.getPrice());
        fakeStoreProductDto.setImage(product.getImageUrl());
        fakeStoreProductDto.setCategory(product.getCategory().getName());
        return fakeStoreProductDto;
    }

    private Product from(FakeStoreProductDto fakeStoreProductDto) {
        Product product = new Product();
        product.setId(fakeStoreProductDto.getId());
        product.setName(fakeStoreProductDto.getTitle());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setPrice(fakeStoreProductDto.getPrice());
        product.setImageUrl(fakeStoreProductDto.getImage());
        Category category = new Category();
        category.setName(fakeStoreProductDto.getCategory());
        product.setCategory(category);
        return product;
    }
}
