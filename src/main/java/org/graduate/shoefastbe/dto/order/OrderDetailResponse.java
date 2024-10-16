package org.graduate.shoefastbe.dto.order;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailResponse {
    private Long id;
    private Double originPrice;
    private Long quantity;
    private Double sellPrice;
    private Long attributeId;
    private Long attributeSize;
    private Long orderId;
}
