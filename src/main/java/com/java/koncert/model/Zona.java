package com.java.koncert.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@IdClass(ZonaPK.class)
@Table(name="zona")
@EqualsAndHashCode
public class Zona  {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
   @Column(name="idzona")
    private int idzona;

    private String naziv;

    @Column(name="kapacitet")
    private int kapacitet;

    @Column(name="cena")
    private int cena;

    @Column(name="preostao_br_karata")
    private int preostaoBrKarata;
    @Id
    @ManyToOne(cascade= {
            //CascadeType.PERSIST,
            CascadeType.MERGE,
            //CascadeType.DETACH,
            // CascadeType.REFRESH,
            CascadeType.REMOVE},fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name="koncert")
    private Koncert koncert;

    // @JsonBackReference(value="zona-karta2")
  //  @OneToMany(mappedBy="zona", cascade = {CascadeType.ALL },fetch = FetchType.EAGER )
 //   private List<Karta> karte;

  //  @JsonBackReference(value = "nekavrednost")




  /*  public void add(Karta tempKarta) {
        if (karte == null) {
            karte = new ArrayList<>();
        }
        karte.add(tempKarta);
        tempKarta.setZona(this);
    }

*/

}
