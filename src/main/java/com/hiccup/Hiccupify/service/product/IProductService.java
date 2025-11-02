package com.hiccup.Hiccupify.service.product;

import com.hiccup.Hiccupify.dto.ProductDto;
import com.hiccup.Hiccupify.model.Category;
import com.hiccup.Hiccupify.model.Product;
import com.hiccup.Hiccupify.request.AddProductRequest;
import com.hiccup.Hiccupify.request.UpdateProductRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest request);
    Product createProduct(AddProductRequest request, Category category);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProductById(UpdateProductRequest updateProductRequest,Long id);
    List<Product> getAllProducts();
    List<Product> getProuductByCategory(String catagory);
    List<Product> getProductByBrand(String brand);
    List<Product> getProductByCatagoryAndBrand(String catagory, String brand);
    List<Product> getProductByName(String name);
    List<Product> getProductByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);

    List<ProductDto> getConvertedProducts(List<Product> product);

    ProductDto convertToDto(Product product);
}
