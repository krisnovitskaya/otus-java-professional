package ru.otus.crm.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "address")
@NoArgsConstructor
@Getter
@Setter
@ToString
//@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "street")
    private String street;

//    @OneToOne(mappedBy = "address")
//    @JoinColumn(name = "client_id")
//    private Client client;

    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }
}
