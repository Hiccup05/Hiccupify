package com.hiccup.Hiccupify.service.product;

import com.hiccup.Hiccupify.dto.ImageDto;
import com.hiccup.Hiccupify.dto.ProductDto;
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
        // logic: check if category exist in db set the product
        //if no then save category first and then save product
//        Category category=request.getCategory();
//      Optional<Category> category1 =  categoryRepository.findById(category.getId());
//        if(category1.isEmpty()){
//            categoryRepository.save(category);
//        }
//       return productRepository.save(createProduct(request,category));

        // look at the return type, the return type of findbyname is category so  need to wrap with optional
        //to use its functions
//        Category category=Optional.ofNullable(categoryRepository.findById(request.getCategory().getId()))
//                .orElseGet(()->{
//            Category newCategory=new Category(request.getCategory().getName());
//            return categoryRepository.save(newCategory);
//        });
        //but while using findbyid which returns optional of category so no need to use wrap ofNullable.
        //using orElseGet is to represent that we are providing solution to null rather that throwing error
        //so on use of orElseThrow
        Category category= Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()->{
                    Category category1=new Category(request.getCategory().getName());
                    return categoryRepository.save(category1);}
                );

        request.setCategory(category);
        return productRepository.save(createProduct(request,category));
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
        //need the explaination why orElse didnt work but orElseThrow worked.
        return productRepository.findById(id).orElseThrow(()->new ResourceNotFound("Product not found"));
    }

    //need the explaination
    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                        ()->{throw new ResourceNotFound("Product not found!");});
    }

    @Override
    public Product updateProductById(UpdateProductRequest updateProductRequest, Long id) {
//        Product updatingProduct=productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Product not found"));
//       return productRepository.save(updateProduct(updatingProduct,updateProductRequest));

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
        //instead of setting the up.category in existing product is to
        //1. to avoid the exception: the category is in transient state so it needs to be persistent stage
        //2. to make it persistance we need to save it which may create the dublicate category issue
        //3. to make persistent we first get it from database and using it to remove creation of dublication
        // entity
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

        //converts image list into stream and use map method that convert each object to another object
        //in our case as its a interface method we are applying the logic that we want to convert
        //image object into image dto and then store in stream as map return stream<R>
        //in our case R is objects of imagedto
        List<ImageDto> imageDtos=images.stream().
                map(image-> modelMapper.map(image, ImageDto.class))
                .toList();

        productDto.setImage(imageDtos);
        return productDto;
    }
}
