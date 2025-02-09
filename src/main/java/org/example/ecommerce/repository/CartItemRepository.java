package org.example.ecommerce.repository;

import org.example.ecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.cartId = :cartId AND ci.product.productId = :productId")
    CartItem findCartItemByProductIdAndCartId(@Param("cartId") Long cartId,@Param("productId") Long productId);


    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.cartId = :cartId AND ci.product.productId = :productId")
    void deleteCartItemByProductIdAndCartId(@Param("cartId") Long cartId,@Param("productId") Long productId);
}
