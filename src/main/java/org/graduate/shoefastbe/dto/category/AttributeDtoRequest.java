package org.graduate.shoefastbe.dto.category;

import lombok.*;

import javax.annotation.security.DenyAll;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AttributeDtoRequest {
    private Integer size;
    private Double price;
    private Integer stock;
}
