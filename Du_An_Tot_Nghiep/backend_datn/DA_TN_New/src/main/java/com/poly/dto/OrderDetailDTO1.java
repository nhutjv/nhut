package com.poly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO1
{
    private Integer id;
    private Integer quantity;
    private Double price;
    private String fullAddress;
    private String nameColor;
    private String nameSize;
    private Integer discountFS;
    private String nameFS;
    private String nameProduct;
}
