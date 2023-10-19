package com.example.mon_thay_the.controller;

import com.example.mon_thay_the.dto.CartItemDto;
import com.example.mon_thay_the.entity.CartItem;
import com.example.mon_thay_the.entity.User;
import com.example.mon_thay_the.exception.UserNotFoundException;
import com.example.mon_thay_the.principal.MyUserDetails;
import com.example.mon_thay_the.request.CartItemRequest;
import com.example.mon_thay_the.response.BaseResponse;
import com.example.mon_thay_the.response.CartResponse;
import com.example.mon_thay_the.response.ListCartItemResponse;
import com.example.mon_thay_the.service.CartItemService;
import com.example.mon_thay_the.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartItemService cartItemService;
    private final UserService userService;


    @GetMapping("/api/cart")
    public ResponseEntity<ListCartItemResponse> getCartItem(@AuthenticationPrincipal MyUserDetails userDetails
            , HttpServletRequest request){

        User user = userDetails.getUser();
        List<CartItem> cartItemList = cartItemService.listCartItems(user);

        List<CartItemDto> cartItemDtoList = new ArrayList<>();
        Double total = 0d;

        for(CartItem c : cartItemList){
            cartItemDtoList.add(new CartItemDto(c));
            total += c.getSubtotal();
        }

        ListCartItemResponse data = new ListCartItemResponse(1, "Get list cart item successfully!", request.getMethod(), HttpStatus.OK.value(),cartItemDtoList, total);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    @PostMapping("/api/cart/add")
    public ResponseEntity<CartResponse> addProductToCart(HttpServletRequest request
            , @AuthenticationPrincipal MyUserDetails userDetails,
            @Valid @RequestBody CartItemRequest cartItemRequest){
        CartItem cartItem = cartItemService.addProduct(cartItemRequest.getId(), cartItemRequest.getQuantity(),userDetails.getUser());

        CartItemDto  cartItemDto = new CartItemDto(cartItem);

        CartResponse data = new CartResponse(1,"Add product to cart successfully!", request.getMethod(), HttpStatus.OK.value(), cartItemDto);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/api/cart/update")
    public ResponseEntity<CartResponse> updateQuantity(HttpServletRequest request
            , @AuthenticationPrincipal MyUserDetails userDetails
            ,@Valid @RequestBody CartItemRequest cartItemRequest){

        CartItem cartItem = cartItemService.updateQuantity(cartItemRequest.getQuantity(), cartItemRequest.getId(), userDetails.getUser());
        CartItemDto cartItemDto = new CartItemDto(cartItem);

        CartResponse data = new CartResponse(1,"Update quantity successfully!", request.getMethod(), HttpStatus.OK.value(), cartItemDto);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @DeleteMapping("/api/cart/delete/{id}")
    public ResponseEntity<BaseResponse> deleteProductFromCart(HttpServletRequest request
            , @AuthenticationPrincipal MyUserDetails userDetails
            , @PathVariable("id") Integer productId){


            cartItemService.removeCartItem(userDetails.getId(),productId);
            BaseResponse data = new BaseResponse(1,"Delete product from cart successfully!", request.getMethod(), HttpStatus.OK.value());
            return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/api/cart/count")
    public Long countCartItem(@AuthenticationPrincipal MyUserDetails userDetails) throws UserNotFoundException {
        return cartItemService.countCartItemByUser(userService.getById(userDetails.getId()));
    }



}
