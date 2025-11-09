package com.codewithmosh.store.controllers;


import com.codewithmosh.store.dtos.CheckoutRequest;
import com.codewithmosh.store.dtos.CheckoutResponse;
import com.codewithmosh.store.dtos.ErrorDto;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.entities.OrderItem;
import com.codewithmosh.store.entities.OrderStatus;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import com.codewithmosh.store.services.AuthService;
import com.codewithmosh.store.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkout")
@AllArgsConstructor
public class CheckoutController {

    private final CartRepository cartRepository;
    private final AuthService authService;
    private final CartService cartService;
    private OrderRepository orderRepository;

    @PostMapping
    public ResponseEntity<?> checkout(
            @Valid @RequestBody CheckoutRequest checkoutRequest
    ) {
        var cart = cartRepository.getCartWithItems(checkoutRequest.getCartId()).orElse(null);
        if (cart == null) {
            return ResponseEntity.badRequest().body(
                    new ErrorDto("Cart not found")
            );
        }

        if (cart.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ErrorDto("Items not found")
            );
        }


        var order = new Order();
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus(OrderStatus.PENDING);
        order.setCustomer(authService.getCurrentUser());

        cart.getItems().forEach(item -> {
            var orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setTotalPrice(item.getTotalPrice());
            orderItem.setUnitPrice(item.getProduct().getPrice());
            order.getItems().add(orderItem);
        });

        orderRepository.save(order);

        cartService.clearCart(cart.getId());

        return ResponseEntity.ok().body(new CheckoutResponse(order.getId()  ));

    }
}
