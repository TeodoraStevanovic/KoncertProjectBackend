package com.java.koncert.repository;

import com.java.koncert.model.Karta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KartaRepository extends JpaRepository<Karta,Integer> {
}
