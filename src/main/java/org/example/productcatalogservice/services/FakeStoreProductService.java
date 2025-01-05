package org.example.productcatalogservice.services;

import org.example.productcatalogservice.clients.FakeStoreApiClient;
import org.example.productcatalogservice.dtos.FakeStoreProductDto;
import org.example.productcatalogservice.models.Category;
import org.example.productcatalogservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// We can add qualifiers, which we can use with @Qualifier with @Autowired
// Refer FakeStoreProductService and ProductController for example
@Service("fks")
public class FakeStoreProductService implements IProductService {
    @Autowired
    private FakeStoreApiClient fakeStoreApiClient;

    public Product getProductById(Long id) {
        return from(fakeStoreApiClient.getProductById(id));
    }

    public List<Product> getAllProducts() {
        List<FakeStoreProductDto> fakeStoreProductDtos = fakeStoreApiClient.getAllProducts();
        if(fakeStoreProductDtos != null) {
            List<Product> productList = new ArrayList<>();
            for (FakeStoreProductDto fakeStoreProductDto : fakeStoreProductDtos) {
                productList.add(from(fakeStoreProductDto));
            }
            return productList;
        }
        return null;
    }

    public Product createProduct(Product product) {
        return from(fakeStoreApiClient.createProduct(from(product)));
    }

    public Product updateProduct(Long productId, Product product) {
        return from(fakeStoreApiClient.replaceProductViaPut(productId, from(product)));
        //return from(fakeStoreApiClient.replaceProductViaPatch(productId, from(product)));
    }

    public Product deleteProductById(Long id) {
        return from(fakeStoreApiClient.deleteProductById(id));
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
        if (fakeStoreProductDto == null) {
            return null;
        }
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
