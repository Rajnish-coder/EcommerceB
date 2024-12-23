package org.example.ecommerce.controller;


import jakarta.validation.Valid;
import org.example.ecommerce.config.AppConstants;
import org.example.ecommerce.payload.CategoryDTO;
import org.example.ecommerce.payload.CategoryResponse;
import org.example.ecommerce.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/category")
    public ResponseEntity<CategoryResponse> getCategories(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORY_BY,required = false) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_ORDER,required = false) String sortOrder) {

        CategoryResponse response = categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/admin/category")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategory = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/category/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable long categoryId) {
        CategoryDTO response = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/admin/category/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable long categoryId) {
        CategoryDTO updatedCategoryDTO = categoryService.updateCategory(categoryDTO,categoryId);
        return new ResponseEntity<>(updatedCategoryDTO, HttpStatus.OK);
    }
}
