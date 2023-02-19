package com.java.koncert.repository;

import com.java.koncert.model.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KorisnikRepository extends JpaRepository<Korisnik,Integer> {
}
