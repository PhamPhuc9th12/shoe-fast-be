package org.graduate.shoefastbe.dto.product;

import lombok.*;
import org.graduate.shoefastbe.entity.AttributeEntity;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductDetailResponse {
    private Long id;
    private String name;
    private String code;
    private String description;
    private String main;
    private Long discount;
    private List<String> images;
    private List<AttributeEntity> attributes;
    private List<Long> categoryIds;
    private Long saleId;
    private Long brandId;
    private String brand;
    private Double price;
    private Long view;


}
