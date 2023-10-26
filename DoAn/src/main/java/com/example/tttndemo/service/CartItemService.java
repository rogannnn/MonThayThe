package com.example.tttndemo.service;

import com.example.tttndemo.entity.CartItem;
import com.example.tttndemo.entity.Product;
import com.example.tttndemo.entity.User;
import com.example.tttndemo.repository.CartItemRepository;
import com.example.tttndemo.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CartItemService {
    private final CartItemRepository cartItemRepo;

    private final ProductRepository productRepo;

    public CartItemService(CartItemRepository cartItemRepo, ProductRepository productRepo) {
        this.cartItemRepo = cartItemRepo;
        this.productRepo = productRepo;
    }

    public List<CartItem> listCartItems(User user){
        return cartItemRepo.findCartItemByUser(user);
    }

    public int addProduct(int productId, int quantity, User user){
        int addedQuantity = quantity;

        Product product = productRepo.findById(productId).get();
        CartItem cartItem = cartItemRepo.findByUserAndProduct(user,product);

        if(cartItem != null){
            addedQuantity += cartItem.getQuantity();
            cartItem.setQuantity(addedQuantity);
        }else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setUser(user);
            cartItem.setQuantity(quantity);
        }
        cartItemRepo.save(cartItem);
        return addedQuantity;
    }

    public Long countCartItemByUser(User user){
        return cartItemRepo.countByUser(user);
    }

    public float updateQuantity(Integer quantity, Integer productId, User user){
        cartItemRepo.updateQuantity(quantity, productId, user.getId());

        Product product = productRepo.findById(productId).get();
        float subtotal = (float)(product.getPrice() * quantity);
        return subtotal;
    }

    public void removeCartItem(Integer userId, Integer productId){
        cartItemRepo.deleteCartItemByUserAndUser(productId, userId);
    }

    public void removeAllCartItem(Integer userId){
        cartItemRepo.deleteAll();
    }
}
