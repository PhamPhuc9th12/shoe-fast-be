package org.graduate.shoefastbe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

@Entity
@Table(name = "attribute")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private Long productId;
    private Long size;
    private Long stock;
    private Long cache;
    private LocalDate createDate;
    private LocalDate modifyDate;
}