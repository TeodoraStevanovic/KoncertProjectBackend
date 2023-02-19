package com.java.koncert.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="promokod")
public class Promokod implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="idpromokod")
    private int idpromokod;

    @Column(name="kod")
    private String kod;

    @Column(name="iskoriscen")
    private int iskoriscen;

    @Column(name="upotrebljiv")
    private int upotrebljiv;


    @JsonBackReference(value="promo-korisnik")
    @ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="korisnik")
    private Korisnik korisnik;

    @JsonBackReference(value="promo-rez")
    @ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="rezervacija")
    private Rezervacija rezervacija;



    public Promokod(String kod, int iskoriscen, int upotrebljiv, Korisnik korisnik, Rezervacija rezervacija) {
        this.kod = kod;
        this.iskoriscen = iskoriscen;
        this.upotrebljiv = upotrebljiv;
        this.korisnik = korisnik;
        this.rezervacija = rezervacija;
    }

}
