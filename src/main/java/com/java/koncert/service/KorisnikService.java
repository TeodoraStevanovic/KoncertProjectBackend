package com.java.koncert.service;

import com.java.koncert.model.Korisnik;
import com.java.koncert.repository.KorisnikRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KorisnikService {
    KorisnikRepository korisnikRepository;

    public KorisnikService(KorisnikRepository korisnikRepository) {
        this.korisnikRepository = korisnikRepository;
    }

    public List<Korisnik> findAll() {
        return korisnikRepository.findAll();
    }


    public Korisnik findById(int theId) {
        Optional<Korisnik> result = korisnikRepository.findById(theId);

        Korisnik theKorisnik = null;

        if (result.isPresent()) {
            theKorisnik = result.get();
        }
        else {
            throw new RuntimeException("Did not find korisnik id - " + theId);
        }
        return theKorisnik;
    }



    public void save(Korisnik korisnik) {korisnikRepository.save(korisnik);}


    public void deleteById(int theId) {
        korisnikRepository.deleteById(theId);

    }
}
