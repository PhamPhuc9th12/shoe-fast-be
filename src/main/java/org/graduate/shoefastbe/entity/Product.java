package org.graduate.shoefastbe.entity;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "tbl_products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String description;
    private String name;
    private Long view;
    private Long brandId;
    private Long saleId;
    private Boolean isActive;
    private LocalDate createDate;
    private LocalDate modifyDate;
}
