package org.example.OnlineShop.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "phones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Phone {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phone_id_seq")
    @SequenceGenerator(name = "phone_id_seq", sequenceName = "phone_id_seq", initialValue = 109, allocationSize = 1)
    private Long id;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "phone_title", nullable = false)
    private String phoneTitle;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "amount", nullable = false)
    private String amount;

    @Column(name = "diaplay", nullable = false)
    private String diaplay;

    @Column(name = "_memory")
    private String memory;

    @Column(name = "ram")
    private String ram;

    @Column(name = "processor", nullable = false)
    private String processor;

    @Column(name = "accumulator", nullable = false)
    private String accumulator;

    @Column(name = "camera", nullable = false)
    private String camera;

    @Column(name = "_case", nullable = false)
    private String _case;
}
