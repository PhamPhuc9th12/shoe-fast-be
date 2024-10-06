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
@Table(name = "account_detail")
public class AccountDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate birthdate;
    @Column(name = "fullname")
    private String fullName;
    private String email;
    private String gender;
    private String phone;
    private Long accountId;
    private String address;
}
