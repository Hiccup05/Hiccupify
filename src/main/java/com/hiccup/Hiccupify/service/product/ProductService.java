package com.hiccup.Hiccupify.service.product;

import com.hiccup.Hiccupify.dto.ImageDto;
import com.hiccup.Hiccupify.dto.ProductDto;
import com.hiccup.Hiccupify.exception.AlreadyExistException;
import com.hiccup.Hiccupify.exception.ResourceNotFound;
import com.hiccup.Hiccupify.model.Category;
import com.hiccup.Hiccupify.model.Image;
import com.hiccup.Hiccupify.model.Product;
import com.hiccup.Hiccupify.repository.CategoryRepository;
import com.hiccup.Hiccupify.repository.ImageRepository;
import com.hiccup.Hiccupify.repository.ProductRepository;
import com.hiccup.Hiccupify.request.AddProductRequest;
import com.hiccup.Hiccupify.request.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Override
    public Product addProduct(AddProductRequest request) {
        if(productExistis(request.getName(), request.getBrand())){
            throw new AlreadyExistException(request.getBrand()+" "+request.getName()+" already exists");
        }
        Category category= Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()->{
                    Category category1=new Category(request.getCategory().getName());
                    return categoryRepository.save(category1);}
                );

        request.setCategory(category);
        return productRepository.save(createProduct(request,category));
    }

    private boolean productExistis(String name, String brandName){
        return productRepository.existsByNameAndBrand(name,brandName);
    }

    @Override
    public Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                request.getCategory()
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(()->new ResourceNotFound("Product not found"));
    }


    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                        ()->{throw new ResourceNotFound("Product not found!");});
    }

    @Override
    public Product updateProductById(UpdateProductRequest updateProductRequest, Long id) {
        return productRepository.findById(id)
                .map(existingProduct->updateProduct(existingProduct,updateProductRequest))
                .map(productRepository :: save)
                .orElseThrow(()->new ResourceNotFound("Product not found!"));
    }

    public Product updateProduct(Product exisitingProduct, UpdateProductRequest up){
        exisitingProduct.setName(up.getName());
        exisitingProduct.setBrand(up.getBrand());
        exisitingProduct.setDescription(up.getDescription());
        exisitingProduct.setPrice(up.getPrice());
        exisitingProduct.setInventory(up.getInventory());
        Category category=categoryRepository.findByName(up.getCategory().getName());
        exisitingProduct.setCategory(category);
        return exisitingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProuductByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductByCatagoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> product){
        return product.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product){
        ProductDto productDto=modelMapper.map(product,ProductDto.class);
        List<Image> images=imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos=images.stream().
                map(image-> modelMapper.map(image, ImageDto.class))
                .toList();

        productDto.setImage(imageDtos);
        return productDto;
    }
}
