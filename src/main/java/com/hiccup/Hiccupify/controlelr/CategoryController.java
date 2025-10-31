package com.hiccup.Hiccupify.controlelr;

import com.hiccup.Hiccupify.exception.AlreadyExistException;
import com.hiccup.Hiccupify.model.Category;
import com.hiccup.Hiccupify.response.ApiResponse;
import com.hiccup.Hiccupify.service.catagory.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse> getAllCategories(){
        try {
            List<Category> categories=categoryService.getALlCategories();
            return ResponseEntity.ok().body(new ApiResponse("categories",categories));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Cannot present categories at the moment",INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/category/add")
    public ResponseEntity<ApiResponse> addCategories(@RequestBody Category name){
        try {
            Category category=categoryService.addCategory(name);
            return ResponseEntity.ok(new ApiResponse("Success",category));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/category/get_by_id/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id){
        try {
            Category category=categoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Fetched successfully",category));
        } catch (RuntimeException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("category/get_by_name/{name}")
    public ResponseEntity<ApiResponse> getById(@PathVariable String name){
        try {
            Category category=categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Fetched successfully",category));
        } catch (RuntimeException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping("category/delete/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id){
        try {
                categoryService.deleteCategory(id);
                return ResponseEntity.ok(new ApiResponse("Successfully deleted",null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PostMapping("/category/update/{id}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id,@RequestBody Category category){
        try {
                Category category1 = categoryService.updateCategory(category, id);
                return ResponseEntity.ok(new ApiResponse("Update successful",category1));

        } catch (RuntimeException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
