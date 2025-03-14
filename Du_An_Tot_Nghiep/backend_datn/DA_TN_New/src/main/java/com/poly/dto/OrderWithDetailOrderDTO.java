package com.poly.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderWithDetailOrderDTO {
	private Integer orderId;
    private Float deliveryFee;
    private Float totalCash;
    private String note;
    private Date updateddDate;
    private String state;
    private List<OrderDetailDTO1> orderDetails;
}
