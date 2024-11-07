package org.graduate.shoefastbe.dto.order;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MonthSynthesis {
    private Long month;
    private Long count;
    private Double total;
}
