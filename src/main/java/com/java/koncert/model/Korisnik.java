package com.java.koncert.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode
@Table(name="korisnik")
public class Korisnik implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="idkorisnik")
    private int idkorisnik;


    @Column(name="ime")
    private String ime;

    @Column(name="prezime")
    private String prezime;

    @Column(name="kompanija")
    private String kompanija;

    @Column(name="adresa1")
    private String adresa1;

    @Column(name="adresa2")
    private String adresa2;

    @Column(name="postanskibr")
    private String postanskibr;

    @Column(name="mesto")
    private String mesto;

    @Column(name="drzava")
    private String drzava;

    @Column(name="email")
    private String email;
    @Column(name="potvrdaemail")
    private String potvrdaemail;
/*
    @JsonManagedReference(value="rezervacija-korisnik")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(mappedBy="korisnik", cascade = CascadeType.ALL ,
            fetch = FetchType.LAZY)
    private List<Rezervacija> rezervacije;

    public void add(Rezervacija rezervacija) {
        if (rezervacije == null) {
            rezervacije = new ArrayList<>();
        }
        rezervacije.add(rezervacija);
        rezervacija.setKorisnik(this);
    }

*/


  /*  @JsonManagedReference(value="promo-korisnik")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(mappedBy="korisnik", cascade = CascadeType.ALL )
    private List<Promokod> promokodovi;

    public void add(Promokod kod) {
        if (promokodovi == null) {
            promokodovi = new ArrayList<>();
        }
        promokodovi.add(kod);
        kod.setKorisnik(this);
    }
*/

}
