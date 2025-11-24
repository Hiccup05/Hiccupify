package com.hiccup.Hiccupify.request;

import com.hiccup.Hiccupify.model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequest {
    //the logic is we don't add a image during adding the new product so image field is excluded..
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
}
