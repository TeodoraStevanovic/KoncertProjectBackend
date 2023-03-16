package com.java.koncert.service;

import com.java.koncert.model.Koncert;
import com.java.koncert.model.Korisnik;
import com.java.koncert.model.Rezervacija;
import com.java.koncert.repository.RezervacijaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RezervacijaService {

    RezervacijaRepository rezervacijaRepository;

    public RezervacijaService(RezervacijaRepository rezervacijaRepository) {
        this.rezervacijaRepository = rezervacijaRepository;
    }
    public List<Rezervacija> findAll() {
        List<Rezervacija> lis=    rezervacijaRepository.findAll();
        return lis;
    }


    public Rezervacija findById(int theId) {
        Optional<Rezervacija> result = rezervacijaRepository.findById(theId);

        Rezervacija rez = null;

        if (result.isPresent()) {
            rez = result.get();
        }
        else {
            throw new RuntimeException("Did not find rez id - " + theId);
        }
        return rez;
    }
    public Rezervacija findByTokenAndKorisnik(Korisnik korisnik,String token) {
        Rezervacija rezervacija = rezervacijaRepository.findByKorisnikAndToken(korisnik,token);
        return rezervacija;
    }


    public void save(Rezervacija rezervacija) {rezervacijaRepository.save(rezervacija);}


    public void deleteById(int theId) {
        rezervacijaRepository.deleteById(theId);
       // rezervacijaRepository.flush();

    }
    public void deleteReservation(Rezervacija r) {
        rezervacijaRepository.delete(r);


    }

    public Rezervacija create(Rezervacija rezervacija) {
        Rezervacija rez=rezervacijaRepository.save(rezervacija);
        return rez;
    }
}
