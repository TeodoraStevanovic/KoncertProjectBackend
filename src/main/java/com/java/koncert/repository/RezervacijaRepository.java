package com.java.koncert.repository;

import com.java.koncert.model.Rezervacija;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RezervacijaRepository extends JpaRepository<Rezervacija,Integer> {

}
