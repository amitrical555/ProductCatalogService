package org.example.productcatalogservice.services;

import org.example.productcatalogservice.dtos.FakeStoreProductDto;
import org.example.productcatalogservice.models.Product;

import java.util.List;

public interface IProductService {
    public Product getProductById(Long id);
    public List<Product> getAllProducts();
    public Product createProduct(Product product);
    public Product updateProduct(Long productId, Product product);
    public Product deleteProductById(Long id);
}
