package org.example.ecommerce.service.impl;

import org.example.ecommerce.exceptions.ResourceNotFoundException;
import org.example.ecommerce.model.Category;
import org.example.ecommerce.model.Product;
import org.example.ecommerce.payload.ProductDTO;
import org.example.ecommerce.payload.ProductResponse;
import org.example.ecommerce.repository.CategoryRepository;
import org.example.ecommerce.repository.ProductRepository;
import org.example.ecommerce.service.FileService;
import org.example.ecommerce.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    private CategoryRepository categoryRepository;

    private FileService fileService;

    private ModelMapper modelMapper;

    @Value("${project.image}")
    private String path;


    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,FileService fileService,
                              ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.fileService = fileService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        Product product = modelMapper.map(productDTO, Product.class);
        product.setImage("default.png");
        double specialPrice = product.getPrice() - ((product.getDiscount()*0.01)* product.getPrice());
        product.setSpecialPrice(specialPrice);
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOList = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOList);
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO> productDTOList = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOList);
        return productResponse;
    }

    @Override
    public ProductResponse searchByKeyword(String keyword) {

        List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%');
        List<ProductDTO> productDTOList = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOList);
        return productResponse;
    }


    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {

        Product savedProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        savedProduct.setProductName(productDTO.getProductName());
        savedProduct.setDescription(productDTO.getDescription());
        savedProduct.setPrice(productDTO.getPrice());
        savedProduct.setDiscount(productDTO.getDiscount());
        savedProduct.setQuantity(productDTO.getQuantity());
        double specialPrice = productDTO.getPrice() - ((productDTO.getDiscount()*0.01)* productDTO.getPrice());
        savedProduct.setSpecialPrice(specialPrice);

        Product updatedProduct = productRepository.save(savedProduct);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public String deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        productRepository.delete(product);
        return "Product deleted successfully";
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        // 1. Get the product from DB
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        // 2. Upload image on server
        // 3. Get the file name of uploaded image
        String fileName = fileService.uploadImage(path,image);

        // 4. Updating the new file name to the product
        productFromDb.setImage(fileName);

        // 5. Saving updated product
        productRepository.save(productFromDb);

        return modelMapper.map(productFromDb, ProductDTO.class);
    }


}
