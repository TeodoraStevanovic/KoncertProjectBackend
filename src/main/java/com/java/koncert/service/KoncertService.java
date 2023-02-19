package com.java.koncert.service;
import com.java.koncert.model.Koncert;
import com.java.koncert.repository.KoncertRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KoncertService {
    KoncertRepository koncertRepository;

    public KoncertService(KoncertRepository koncertRepository) {
        this.koncertRepository = koncertRepository;
    }
    public List<Koncert> findAll() {
        return koncertRepository.findAll();
    }


    public Koncert findById(int theId) {
        Optional<Koncert> result = koncertRepository.findById(theId);

        Koncert koncert = null;

        if (result.isPresent()) {
            koncert = result.get();
        }
        else {
            throw new RuntimeException("Did not find koncert id - " + theId);
        }
        return koncert;
    }



    public void save(Koncert koncert) {koncertRepository.save(koncert);}


    public void deleteById(int theId) {
        koncertRepository.deleteById(theId);

    }
}
