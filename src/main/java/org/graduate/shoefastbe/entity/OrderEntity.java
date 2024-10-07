package org.graduate.shoefastbe.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String fullname;
    private String phone;
    private String address;
    private String email;
    private Double total;
    private String note;
    private String payment;
    private String shipment;
    private String code;
    private String description;
    private Date shipDate;
    private LocalDate createDate;
    private LocalDate modifyDate;
    private Boolean isPending;
    private String encodeUrl;
    private Boolean seen;
    private Long accountId;
    private Long voucherId;
    private Long orderStatusId;
}
