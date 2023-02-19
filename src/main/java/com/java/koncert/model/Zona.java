package com.java.koncert.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@IdClass(ZonaPK.class)
@Table(name="zona")
public class Zona implements Serializable {

    @Id
   @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="idzona")
    private int idzona;


    @Column(name="naziv")
    private String naziv;

    @Column(name="kapacitet")
    private int kapacitet;

    @Column(name="cena")
    private int cena;

    @Column(name="preostao_br_karata")
    private int preostaoBrKarata;
   // @JsonBackReference(value="zona-karta2")
  //  @OneToMany(mappedBy="zona", cascade = {CascadeType.ALL },fetch = FetchType.EAGER )
 //   private List<Karta> karte;

  //  @JsonBackReference(value = "nekavrednost")
    @Id
    @ManyToOne(cascade= {
            //CascadeType.PERSIST,
            CascadeType.MERGE,
            //CascadeType.DETACH,
           // CascadeType.REFRESH,
            CascadeType.REMOVE},fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name="koncert")
    private Koncert koncert;
////////////////////////////////////////

    public Zona(String naziv, int kapacitet, int cena, int preostaoBrKarata) {
        this.naziv = naziv;
        this.kapacitet = kapacitet;
        this.cena = cena;
        this.preostaoBrKarata = preostaoBrKarata;
    }



  /*  public void add(Karta tempKarta) {
        if (karte == null) {
            karte = new ArrayList<>();
        }
        karte.add(tempKarta);
        tempKarta.setZona(this);
    }

*/

}
