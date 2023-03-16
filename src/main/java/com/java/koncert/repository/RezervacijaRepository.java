package com.java.koncert.repository;

import com.java.koncert.model.Korisnik;
import com.java.koncert.model.Rezervacija;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RezervacijaRepository extends JpaRepository<Rezervacija,Integer> {
    Rezervacija findByKorisnikAndToken(Korisnik k, String token);

    @Override
    void delete(Rezervacija entity);
}
