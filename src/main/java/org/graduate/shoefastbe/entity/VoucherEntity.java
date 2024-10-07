package org.graduate.shoefastbe.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "vouchers")
public class VoucherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Long count;
    private Long discount;
    private LocalDate createDate;
    private LocalDate expireDate;
    private Boolean isActive;
}
