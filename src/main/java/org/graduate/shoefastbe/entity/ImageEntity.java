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
@Table(name = "images")
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageLink;
    private Boolean isActive;
    private String name;
    private Long productId;
    private LocalDate crateDate;
    private LocalDate modifyDate;
}
