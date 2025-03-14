package com.poly.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.poly.model.Order;
import com.poly.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOrderCancelDTO {
    private Integer userId;
    private String fullName;
//    private Long cancelledOrdersCount;

//    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
//    private Date cancellationDateTime;
//
//    private  Double sumTotal;

//    private  Long TotalOrders;
//    private List<Order> cancelledOrders;

    private Long cancelledOrdersCount;
    private Double cancelledOrdersTotalCash;
    private Long totalOrdersCount;
//    private Float cancellationRate;
}
