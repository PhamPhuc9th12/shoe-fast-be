package org.graduate.shoefastbe.dto.attribute;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AttributeDtoResponse {
    private Long id;
    private String name;
    private Double price;
    private Long productId;
    private Long size;
    private Integer stock;
    private Integer cache;
    private LocalDate createDate;
    private LocalDate modifyDate;
}
