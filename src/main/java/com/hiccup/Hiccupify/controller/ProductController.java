package com.hiccup.Hiccupify.controller;

import com.hiccup.Hiccupify.dto.ProductDto;
import com.hiccup.Hiccupify.model.Product;
import com.hiccup.Hiccupify.request.AddProductRequest;
import com.hiccup.Hiccupify.request.UpdateProductRequest;
import com.hiccup.Hiccupify.response.ApiResponse;
import com.hiccup.Hiccupify.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProduct(){
        try {
            List<Product> products=productService.getAllProducts();
            List<ProductDto> convertedProducts=productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Retrieve successful",convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/product/id/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id){
        try {
            Product product=productService.getProductById(id);
            ProductDto convertedProduct=productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Retrieve Successful",product));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteProductById(@PathVariable Long id){
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok(new ApiResponse("Deletion Successful",null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/name")
    public ResponseEntity<ApiResponse> getProductsByName(@RequestParam String name){
        try {
            List<Product> products = productService.getProductByName(name);
            List<ProductDto> convertedProducts=productService.getConvertedProducts(products);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Products not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Retrieve Successfully", convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),INTERNAL_SERVER_ERROR));

        }
    }

    @GetMapping("/category")
    public ResponseEntity<ApiResponse> getProductsByCategory(@RequestParam String category){
        try {
            List<Product> products = productService.getProuductByCategory(category);
            List<ProductDto> convertedProducts=productService.getConvertedProducts(products);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Products not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Retrieve Successfully",convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/brand")
    public ResponseEntity<ApiResponse> getProductsByBrand(@RequestParam String brand){
        try {
            List<Product> products = productService.getProductByBrand(brand);
            List<ProductDto> convertedProducts=productService.getConvertedProducts(products);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Products not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Retrieve Successfully",convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/category_brand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category,@RequestParam String brand){
        try {
            List<Product> products = productService.getProductByCatagoryAndBrand(category,brand);
            List<ProductDto> convertedProducts=productService.getConvertedProducts(products);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Products not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Retrieve Successfully",convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/brand_name")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(@RequestParam String brand,@RequestParam String name){
        try {
            List<Product> products = productService.getProductByCatagoryAndBrand(brand,name);
            List<ProductDto> convertedProducts=productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Retrieve Successfully",convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/count/brand_name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand,@RequestParam String name){
        try {
            var count = productService.countProductsByBrandAndName(brand,name);
            return ResponseEntity.ok(new ApiResponse("Retrieve Successfully",count));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(),null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest productRequest){
        try{
            Product product = productService.addProduct(productRequest);
            return ResponseEntity.ok(new ApiResponse("Addition of product successful",product));
        }
        catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage()+"hello konichiwa",INTERNAL_SERVER_ERROR));
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest productRequest,@PathVariable Long id){
        try{
            Product product = productService.updateProductById(productRequest,id);
            return ResponseEntity.ok(new ApiResponse("Addition of product successful",product));
        }
        catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),INTERNAL_SERVER_ERROR));
        }
    }
}
