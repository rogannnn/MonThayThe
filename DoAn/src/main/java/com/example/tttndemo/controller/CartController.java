package com.example.tttndemo.controller;

import com.example.tttndemo.authentication.MyUserDetails;
import com.example.tttndemo.dto.CartDTO;
import com.example.tttndemo.dto.ProductDTO;
import com.example.tttndemo.entity.CartItem;
import com.example.tttndemo.entity.Product;
import com.example.tttndemo.entity.User;
import com.example.tttndemo.exception.UserNotFoundException;
import com.example.tttndemo.service.CartItemService;
import com.example.tttndemo.service.ProductService;
import com.example.tttndemo.service.PromotionDetailService;
import com.example.tttndemo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    private final CartItemService cartItemService;
    private final UserService userService;

    private final ProductService productService;
    private final PromotionDetailService promotionDetailService;

    public CartController(CartItemService cartItemService, UserService userService, ProductService productService, PromotionDetailService promotionDetailService) {
        this.cartItemService = cartItemService;
        this.userService = userService;
        this.productService = productService;
        this.promotionDetailService = promotionDetailService;
    }


    @GetMapping("/cart")
    public String getCartPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(auth);
        List<CartItem> cartItems = cartItemService.listCartItems(user);
        List<CartDTO> cartDtos = new ArrayList<>();

        for(CartItem c : cartItems){
            cartDtos.add(new CartDTO(c,
                    new ProductDTO(promotionDetailService.findDiscountByProductId(c.getProduct().getId()),c.getProduct())));
        }

        model.addAttribute("cart_quantity", cartItemService.countCartItemByUser(user));
        model.addAttribute("cartItems", cartDtos);
        model.addAttribute("pageTitle", "Shopping Cart");
        return "cart";
    }

    @PostMapping("/cart/add/{pid}/{qty}")
    @ResponseBody
    public String addProductToCart(@PathVariable("pid") Integer productId,
                                   @PathVariable("qty") Integer quantity,
                                   @AuthenticationPrincipal MyUserDetails myUserDetails) {

        if (myUserDetails == null) {
            return "Bạn cần đăng nhập để sử dụng chức năng giỏ hàng";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(auth);
        int addedQuantity = cartItemService.addProduct(productId, quantity, user);

        return "Sản phẩm được thêm thành công vào giỏ hàng";
    }

    @PostMapping("/cart/update/{pid}/{qty}")
    @ResponseBody
    public String updateQuantity(@PathVariable("pid") Integer productId,
                                 @PathVariable("qty") Integer quantity,
                                 @AuthenticationPrincipal MyUserDetails myUserDetails) {

        if (myUserDetails == null) {
            return "Bạn cần đăng nhập để sử dụng chức năng giỏ hàng";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(auth);
        float subtotal = cartItemService.updateQuantity(quantity, productId, user);

        return String.valueOf(subtotal);
    }


    @PostMapping("/cart/delete/{pid}")
    @ResponseBody
    public String deleteCartItem(@PathVariable("pid") Integer productId,
                                 @AuthenticationPrincipal MyUserDetails myUserDetails) {

        if (myUserDetails == null) {
            return "Bạn cần đăng nhập để có thể xóa sản phẩm từ giỏ hàng.";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(auth);
        cartItemService.removeCartItem(user.getId(), productId);

        return "The product have been removed from your shopping cart";
    }

    @GetMapping("/cart/count")
    @ResponseBody
    public Long countItems() throws UserNotFoundException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(auth);
        if(user == null ){
            return null;
        }
        return cartItemService.countCartItemByUser(userService.getById(user.getId()));
    }


    @GetMapping("/cart/check-quantity-product")
    @ResponseBody
    public boolean isUpdateQuantityLessThanInStock(@RequestParam("quantity") Integer quantity,
                                                  @RequestParam("product") Integer productId){

        Product product = productService.getProductById(productId);

        return quantity <= product.getInStock();
    }
}
