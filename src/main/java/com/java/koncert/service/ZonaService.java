package com.java.koncert.service;

import com.java.koncert.model.Koncert;
import com.java.koncert.model.Zona;
import com.java.koncert.model.ZonaPK;
import com.java.koncert.repository.KoncertRepository;
import com.java.koncert.repository.ZonaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ZonaService {

ZonaRepository zonaRepository;
KoncertRepository koncertRepository;

    public ZonaService(ZonaRepository zonaRepository, KoncertRepository koncertRepository) {
        this.zonaRepository = zonaRepository;
        this.koncertRepository = koncertRepository;
    }

    public List<Zona> findAll() {

         List<Zona> zone=zonaRepository.findAll();
         return zone;
    }


    public Zona findById(int theId) {
        Koncert koncert=findKoncertForZona(theId);
      //Optional<Zona> result = Optional.ofNullable(zonaRepository.findByIdzona(theId));
        Optional<Zona> result = zonaRepository.findById(new ZonaPK(theId,koncert.getIdkoncert()));
        Zona zona = null;

        if (result.isPresent()) {
            zona = result.get();
       Koncert k=   zona.getKoncert();
         //   Koncert kon=zona.getId().getIdkoncert();
         //
        zona.setKoncert(k);
        }
        else {
            throw new RuntimeException("Did not find ZONA id - " + theId);
        }
        return zona;
    }

    private Koncert findKoncertForZona(int theId) {
        List<Zona>  lista=zonaRepository.findAll();
        Koncert trazKoncert=null;
        for (Zona z: lista) {
            if (z.getZonaPK().getIdzona()==theId)
            {trazKoncert=z.getKoncert();}

        }
        return trazKoncert;
    }


    public void save(Zona z) {zonaRepository.save(z);}



}
