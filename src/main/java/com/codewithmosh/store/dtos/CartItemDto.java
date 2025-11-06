package com.codewithmosh.store.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {
    public CartProductDto product;
    public int quantity;
    public BigDecimal totalPrice;
}
