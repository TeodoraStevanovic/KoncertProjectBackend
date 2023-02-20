package com.java.koncert.service;

import com.java.koncert.model.Karta;
import com.java.koncert.model.Koncert;
import com.java.koncert.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KartaService {
    KartaRepository kartaRepository;


    public KartaService(KartaRepository kartaRepository) {
        this.kartaRepository = kartaRepository;

    }

    public List<Karta> findAll() {
        return kartaRepository.findAll();
    }


    public Karta findById(int theId) {
        Optional<Karta> result = kartaRepository.findById(theId);

        Karta k = null;

        if (result.isPresent()) {
            k = result.get();
        }
        else {
            throw new RuntimeException("Did not find karta id - " + theId);
        }
        return k;
    }



    public void save(Karta k) {kartaRepository.save(k);}


    public void deleteById(int theId) {
        kartaRepository.deleteById(theId);

    }
}
