package com.java.koncert.service;

import com.java.koncert.model.Koncert;
import com.java.koncert.model.Zona;
import com.java.koncert.model.ZonaPK;
import com.java.koncert.repository.ZonaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ZonaService {

ZonaRepository zonaRepository;

    public ZonaService(ZonaRepository zonaRepository) {
        this.zonaRepository = zonaRepository;
    }

    public List<Zona> findAll() {
        return zonaRepository.findAll();
    }


    public Zona findById(int theId) {
        Optional<Zona> result = Optional.ofNullable(zonaRepository.findByIdzona(theId));

        Zona zona = null;

        if (result.isPresent()) {
            zona = result.get();
        Koncert k=    zona.getKoncert();
        zona.setKoncert(k);
        }
        else {
            throw new RuntimeException("Did not find ZONA id - " + theId);
        }
        return zona;
    }



    public void save(Zona z) {zonaRepository.save(z);}


    public void deleteById(int theId) {
        zonaRepository.deleteZonaByIdzona(theId);

    }
}
