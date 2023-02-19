package com.java.koncert.service;

import com.java.koncert.model.Koncert;
import com.java.koncert.model.Promokod;
import com.java.koncert.repository.PromokodRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromokodService {

    PromokodRepository promokodRepository;

    public PromokodService(PromokodRepository promokodRepository) {
        this.promokodRepository = promokodRepository;
    }

    public List<Promokod> findAll() {
        return promokodRepository.findAll();
    }


    public Promokod findById(int theId) {
        Optional<Promokod> result = promokodRepository.findById(theId);

        Promokod p = null;

        if (result.isPresent()) {
            p = result.get();
        }
        else {
            throw new RuntimeException("Did not find promokod id - " + theId);
        }
        return p;
    }



    public void save(Promokod p) {promokodRepository.save(p);}


    public void deleteById(int theId) {
        promokodRepository.deleteById(theId);

    }

    public Promokod update(Promokod promokod) {
        promokodRepository.save(promokod);
        return promokod;
    }
}
