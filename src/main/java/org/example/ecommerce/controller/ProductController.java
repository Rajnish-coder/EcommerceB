package org.example.ecommerce.controller;

import org.example.ecommerce.payload.ProductDTO;
import org.example.ecommerce.payload.ProductResponse;
import org.example.ecommerce.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/admin/category/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO,
                                                 @PathVariable Long categoryId) {

        ProductDTO response = productService.addProduct(productDTO,categoryId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts() {

        ProductResponse productResponse = productService.getAllProducts();
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/category/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId) {

        ProductResponse productResponse = productService.searchByCategory(categoryId);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword) {

        ProductResponse productResponse = productService.searchByKeyword(keyword);
        return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO,
                                                    @PathVariable Long productId) {

        ProductDTO updatedProductDTO = productService.updateProduct(productId,productDTO);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }


    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        String response = productService.deleteProduct(productId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("Image") MultipartFile image ) throws IOException {
       ProductDTO productDTO =  productService.updateProductImage(productId,image);
       return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }


}