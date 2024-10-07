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
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String description;
    private String name;
    private Long view;
    private Long branchId;
    private Long saleId;
    private Boolean isActive;
    private LocalDate createDate;
    private LocalDate modifyDate;
}
