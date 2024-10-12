package org.graduate.shoefastbe.dto.order;

import lombok.*;
import org.graduate.shoefastbe.entity.OrderDetailEntity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDtoRequest {
    private String fullName;
    private String phone;
    private String address;
    private String note;
    private Double total;
    private String email;
    private Boolean isPending;
    private Long accountId;
    private Collection<OrderDetailEntity> orderDetails;
    private String code;
    private String payment;
}
