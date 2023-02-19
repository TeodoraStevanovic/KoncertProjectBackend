package com.java.koncert.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode
@ToString
@Table(name="koncert")
public class Koncert implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="idkoncert")
    private int idkoncert;


    @Column(name="naziv")
    private String naziv;
    @Column(name="grad")
    private String grad;
    @Column(name="lokacija")
    private String lokacija;

    @Column(name="datum")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date datum;
/*
    @JsonManagedReference(value="zona-koncert")
    @OneToMany(mappedBy="koncert", cascade = CascadeType.ALL  )
    private List<Zona> zone;

    public void add(Zona zona) {
        if (zone == null) {
            zone = new ArrayList<>();
        }
        zone.add(zona);
        zona.setKoncert(this);
    }
*/
    public Koncert(String naziv, String grad, String lokacija, Date datum) {
        this.naziv = naziv;
        this.grad = grad;
        this.lokacija = lokacija;
        this.datum = datum;
    }
}
