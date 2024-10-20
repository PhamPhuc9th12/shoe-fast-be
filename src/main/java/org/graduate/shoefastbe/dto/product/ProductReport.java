package org.graduate.shoefastbe.dto.product;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductReport {
    private Long id;
    private String name;
    private Double amount;
    private Long quantity;
    private Long count;
}
