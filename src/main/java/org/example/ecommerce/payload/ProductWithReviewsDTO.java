package org.example.ecommerce.payload;

import org.example.ecommerce.external.Review;
import org.example.ecommerce.model.Product;

import java.util.List;

public class ProductWithReviewsDTO {

    private ProductDTO product;
    private List<Review> reviews;

    public ProductWithReviewsDTO() {
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
