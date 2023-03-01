package com.java.koncert.repository;

import com.java.koncert.model.Rezervacija;
import com.java.koncert.model.Zona;
import com.java.koncert.model.ZonaPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZonaRepository extends JpaRepository<Zona,ZonaPK> {
   // Zona findByIdzona(int id);
  //  Zona findByKoncert(int id);

  //  void deleteZonaByIdzona(int idzona);
}
