package org.graduate.shoefastbe.dto.order;

import lombok.*;
import org.graduate.shoefastbe.dto.attribute.AttributeDtoResponse;

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
    private AttributeDtoResponse attribute;
    private Long attributeSize;
    private Long orderId;
}
