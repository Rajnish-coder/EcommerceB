package org.example.ecommerce.repository;

import org.example.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c WHERE c.user.email = :email")
    Cart findCartByEmail(@Param("email") String email);

    @Query("SELECT c FROM Cart c WHERE c.user.email = :email AND c.cartId = :cartId")
    Cart findCartByEmailAndCartId(@Param("email") String email,@Param("cartId") Long cartId);

    @Query("SELECT c FROM Cart c JOIN FETCH c.cartItems ci JOIN FETCH ci.product p WHERE p.productId = ?1")
    List<Cart> findCartsByProductId(Long productId);
}
