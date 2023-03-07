package com.java.koncert.model;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode
@Table(name="rezervacija")

public class Rezervacija implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="idrezervacija")
    private int idrezervacija;

   // @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JoinColumn(name="placed_at")
    private LocalDateTime placedAt =LocalDateTime.now();
    @Column(name="broj_karata")
    private int brojKarata;
    @Column(name="ukupno")
    private double ukupno;
    @Column(name="token")
    private String token;

  // @JsonBackReference(value="rezervacija-korisnik1")
    @ManyToOne(cascade= {CascadeType.REFRESH,CascadeType.MERGE},fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name="idkorisnik")
    private Korisnik korisnik;


    public Rezervacija(int brojKarata, Korisnik korisnik) {
        this.brojKarata = brojKarata;
        this.korisnik = korisnik;
    }

/*
    @JsonManagedReference(value="promo-rezervacija")
    @OneToMany(mappedBy="rezervacija", cascade = CascadeType.ALL )
    private List<Promokod> promokodovi;

    public void add(Promokod kod) {
        if (promokodovi == null) {
            promokodovi = new ArrayList<>();
        }
        promokodovi.add(kod);
        kod.setRezervacija(this);
    }


 */

   // @JsonManagedReference (value="rez-karte")
  //  @OneToMany(mappedBy = "rezervacija", cascade = CascadeType.ALL)
//private List<Karta> karte;

}
