package com.dto;

import lombok.Data;

@Data
public class VehicleInfoDTO {
    private String name;
    private String year;
    // image 如何处理
    private Long seat;
   // private final String powerMode = "Gasoline";
    //private final Double mpg = 1.0;
   // private final String driveMode = "Manual";
    private String pricePerDay;
}
