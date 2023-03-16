package com.java.koncert.responseClass;

import com.java.koncert.model.Karta;
import com.java.koncert.model.Rezervacija;
import com.java.koncert.model.Zona;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseRezervacijaKarte {
    Rezervacija rezervacija;
    List<Karta> karte=new ArrayList<>();
    Zona zona;

}
