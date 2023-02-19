package com.java.koncert.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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


  // @JsonBackReference(value="rezervacija-korisnik1")
    @ManyToOne(cascade= {CascadeType.MERGE,
            CascadeType.REMOVE},fetch = FetchType.EAGER,optional = false)
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

   // @JsonBackReference(value="kartaa-zona")
    @ManyToOne(cascade= {CascadeType.MERGE})
    //@JoinColumns({@JoinColumn(name="zona"), @JoinColumn(name="koncert")})
    @JoinColumn(name="zona")
    @JoinColumn(name="koncert")
    private Zona zona;
  //  @JsonBackReference(value="kartaa-koncert")
    @ManyToOne(cascade= {//CascadeType.PERSIST,
            CascadeType.MERGE,
            // CascadeType.DETACH,
            //  CascadeType.REFRESH
    })
    @JoinColumn(name="koncert",insertable = false, updatable = false)
    private Koncert koncert;
}
