package com.java.koncert.model;
import lombok.*;
import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode
@ToString
@Table(name="karta")
public class Karta {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="idkarta")
    private int idkarta;
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

   // @JsonBackReference(value="rezervacija-karte")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rezervacija")
    private Rezervacija rezervacija;
}
