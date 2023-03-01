package com.java.koncert.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;


@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
//@IdClass(ZonaPK.class)
@Table(name="zona")
@EqualsAndHashCode
public class Zona  {
    @EmbeddedId
    protected ZonaPK zonaPK;
    //@Id
   // @GeneratedValue(strategy= GenerationType.AUTO)
   //@Column(name="idzona")
    //private int idzona;
   // @Id
    @ManyToOne(cascade= {
            //CascadeType.PERSIST,
            CascadeType.MERGE,
            //CascadeType.DETACH,
            // CascadeType.REFRESH,
            CascadeType.REMOVE})
    @JoinColumn(name="koncert",insertable = false, updatable = false)
    private Koncert koncert;

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




  /*  public void add(Karta tempKarta) {
        if (karte == null) {
            karte = new ArrayList<>();
        }
        karte.add(tempKarta);
        tempKarta.setZona(this);
    }

*/

}
