package com.java.koncert.repository;

import com.java.koncert.model.Koncert;
import com.java.koncert.model.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KoncertRepository  extends JpaRepository<Koncert,Integer> {
}
