package com.myapp.socksapijavabeginner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SockDto {

    private Long id;
    private String color;
    private int cottonPercentage;
    private int quantity;
}
