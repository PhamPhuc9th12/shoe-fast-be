package org.graduate.shoefastbe.entity;

import lombok.*;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "order_status")
public class OrderStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private String name;
    private LocalDate createDate;
    private LocalDate updateDate;
}
