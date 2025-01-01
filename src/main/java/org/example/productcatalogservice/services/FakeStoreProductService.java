package org.example.productcatalogservice.services;

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
    // @Autowired inject the singleton object of the specified dependency
    // Earlier we used to create a parameterized constructor for it.
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    public Product getProductById(Long id) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        // restTemplate.getForObject directly returns the object,
        // where as restTemplate.getForEntity returns a response
        // with additional details, e.g HttpStatusCode, ResponseBody. Headers etc.
//        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity =
//                restTemplate.getForEntity(
//                        "https://fakestoreapi.com/products/{id}",
//                        FakeStoreProductDto.class,
//                        id);
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity =
                this.requestForEntity(
                        "https://fakestoreapi.com/products/{id}",
                        HttpMethod.GET,
                        null,
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

        // Note: Here we can't use List<FakeStoreProductDto> as type of List is
        // inferred after assignment, for e.g. List.of(1,2), List.of("First", "Second") etc.
        // Generic types are determined only after inserting elements.
        // We can't see datatype using li.getClass().getName().
        // Hence we shall use primitive type i.e. FakeStoreProductDto[].
        // "Tel aise na nikale to waise nikal lo"
//        ResponseEntity<FakeStoreProductDto[]> fakeStoreProductListEntity =
//                restTemplate.getForEntity(
//                        "https://fakestoreapi.com/products",
//                        FakeStoreProductDto[].class);
        ResponseEntity<FakeStoreProductDto[]> fakeStoreProductListEntity =
                this.requestForEntity(
                        "https://fakestoreapi.com/products",
                        HttpMethod.GET,
                        null,
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
//        ResponseEntity<FakeStoreProductDto> responseEntity =
//                restTemplate.postForEntity(
//                        "https://fakestoreapi.com/products",
//                        fakeStoreProductDto,
//                        FakeStoreProductDto.class
//                );
        ResponseEntity<FakeStoreProductDto> responseEntity =
                this.requestForEntity(
                        "https://fakestoreapi.com/products",
                        HttpMethod.POST,
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

    // Using PUT method call
    /*
    public Product updateProduct(Long productId, Product product) {
        FakeStoreProductDto fakeStoreProductDto = from(product);
        RestTemplate restTemplate = restTemplateBuilder.build();

        // Calling below line does not return ResponseEntity,
        // and there is no putForEntity method.
        // So we shall copy implementation of postForEntity method
        // in our class and update it for all HttpMethod support.
//        restTemplate.put(
//                "https://fakestoreapi.com/products/{productId}",
//                fakeStoreProductDto,
//                product.getId()
//        );
//        return product;

        ResponseEntity<FakeStoreProductDto> responseEntity = this.requestForEntity(
                "https://fakestoreapi.com/products/{productId}",
                HttpMethod.PUT,
                fakeStoreProductDto,
                FakeStoreProductDto.class,
                productId
        );
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)
            && responseEntity.getBody() != null) {
            return from(responseEntity.getBody());
        }
        return null;
    }
    */

    // Using PATCH method call
    public Product updateProduct(Long productId, Product product) {
        FakeStoreProductDto fakeStoreProductDto = from(product);
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDto> responseEntity = this.requestForEntity(
                "https://fakestoreapi.com/products/{productId}",
                HttpMethod.PATCH,
                fakeStoreProductDto,
                FakeStoreProductDto.class,
                productId
        );
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)
                && responseEntity.getBody() != null) {
            return from(responseEntity.getBody());
        }
        return null;
    }

    public Product deleteProductById(Long id) {
        RestTemplate restTemplate = restTemplateBuilder.build();
//        restTemplate.delete(
//                "https://fakestoreapi.com/products/{productId}",
//                id
//        );
//        return null;

        ResponseEntity<FakeStoreProductDto> responseEntity = this.requestForEntity(
                "https://fakestoreapi.com/products/{productId}",
                HttpMethod.DELETE,
                null,
                FakeStoreProductDto.class,
                id
        );
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)
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

    // ****** Below methods are taken from RestTemplate and modified ****** //

    private <T> ResponseEntity<T> requestForEntity(String url, HttpMethod method, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return (ResponseEntity)nonNull((ResponseEntity)restTemplate.execute(url, method, requestCallback, responseExtractor, uriVariables));
    }

    private <T> ResponseEntity<T> requestForEntity(String url, HttpMethod method, @Nullable Object request, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return (ResponseEntity)nonNull((ResponseEntity)restTemplate.execute(url, method, requestCallback, responseExtractor, uriVariables));
    }

    private <T> ResponseEntity<T> methodForEntity(URI url, HttpMethod method, @Nullable Object request, Class<T> responseType) throws RestClientException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return (ResponseEntity)nonNull((ResponseEntity)restTemplate.execute(url, method, requestCallback, responseExtractor));
    }

    private static <T> T nonNull(@Nullable T result) {
        Assert.state(result != null, "No result");
        return result;
    }
}
