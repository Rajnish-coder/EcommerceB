package org.example.ecommerce.service.impl;

import org.example.ecommerce.exceptions.APIException;
import org.example.ecommerce.exceptions.ResourceNotFoundException;
import org.example.ecommerce.model.Cart;
import org.example.ecommerce.model.Category;
import org.example.ecommerce.model.Product;
import org.example.ecommerce.payload.CartDTO;
import org.example.ecommerce.payload.ProductDTO;
import org.example.ecommerce.payload.ProductResponse;
import org.example.ecommerce.repository.CartRepository;
import org.example.ecommerce.repository.CategoryRepository;
import org.example.ecommerce.repository.ProductRepository;
import org.example.ecommerce.service.CartService;
import org.example.ecommerce.service.FileService;
import org.example.ecommerce.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    private CategoryRepository categoryRepository;

    private FileService fileService;

    private ModelMapper modelMapper;

    private CartRepository cartRepository;

    private CartService cartService;

    @Value("${project.image}")
    private String path;


    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,FileService fileService,
                              ModelMapper modelMapper, CartRepository cartRepository, CartService cartService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.fileService = fileService;
        this.modelMapper = modelMapper;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
    }

    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        Product productSearch = productRepository.findByProductName(productDTO.getProductName());
        if(productSearch != null) {
            throw new APIException("Product with this name already exists");
        }
        Product product = modelMapper.map(productDTO, Product.class);
        product.setImage("default.png");
        double specialPrice = product.getPrice() - ((product.getDiscount()*0.01)* product.getPrice());
        product.setSpecialPrice(specialPrice);
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Product> productPage = productRepository.findAll(pageable);
        List<ProductDTO> productDTOList = productPage.getContent().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        if(productDTOList.isEmpty()) {
            throw new APIException("No products found");
        }
        return getProductResponse(productPage, productDTOList);
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Product> productPage = productRepository.findByCategoryOrderByPriceAsc(category,pageable);
        if(productPage.isEmpty()) {
            throw new APIException("No products found");
        }
        List<ProductDTO> productDTOList = productPage.getContent().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        return getProductResponse(productPage, productDTOList);
    }


    @Override
    public ProductResponse searchByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%',pageable);
        if(productPage.isEmpty()) {
            throw new APIException("No products found");
        }
        List<ProductDTO> productDTOList = productPage.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        return getProductResponse(productPage, productDTOList);
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

        List<Cart> carts = cartRepository.findCartsByProductId(productId);

        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<ProductDTO> products = cart.getCartItems().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

            cartDTO.setProducts(products);

            return cartDTO;

        }).toList();

        cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));

        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public String deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        productRepository.delete(product);
        // DELETE
        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

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

    private ProductResponse getProductResponse(Page<Product> productPage, List<ProductDTO> productDTOList) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalpages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setLastPage(productPage.isLast());
        productResponse.setContent(productDTOList);
        return productResponse;
    }

}
