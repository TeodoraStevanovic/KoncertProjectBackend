package com.java.koncert.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ZonaPK implements Serializable {
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="idzona")
    private int idzona;

    //private Koncert koncert;
    @Column(name = "koncert")
    protected int koncert;

}
