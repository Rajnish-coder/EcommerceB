package org.example.ecommerce.payload;

public class CartItemDTO {

    private Long cartItemId;

    private CartDTO cart;

    private ProductDTO product;

    private Integer quantity;

    private Double discount;

    private Double totalPrice;

    public CartItemDTO() {
    }

    public CartItemDTO(Long cartItemId, CartDTO cart, ProductDTO product, Integer quantity, Double discount, Double totalPrice) {
        this.cartItemId = cartItemId;
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.discount = discount;
        this.totalPrice = totalPrice;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public CartDTO getCart() {
        return cart;
    }

    public void setCart(CartDTO cart) {
        this.cart = cart;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
