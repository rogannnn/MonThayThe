package com.example.tttndemo.controller;

import com.example.tttndemo.authentication.MyUserDetails;
import com.example.tttndemo.entity.CartItem;
import com.example.tttndemo.entity.User;
import com.example.tttndemo.exception.UserNotFoundException;
import com.example.tttndemo.service.CartItemService;
import com.example.tttndemo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CartController {

    private final CartItemService cartItemService;
    private final UserService userService;

    public CartController(CartItemService cartItemService, UserService userService) {
        this.cartItemService = cartItemService;
        this.userService = userService;
    }


    @GetMapping("/cart")
    public String getCartPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentlyLoggedInUser(auth);
        List<CartItem> cartItems = cartItemService.listCartItems(user);

        model.addAttribute("cart_quantity", cartItemService.countCartItemByUser(user));
        model.addAttribute("cartItems", cartItems);
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
}
