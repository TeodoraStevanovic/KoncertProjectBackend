package com.java.koncert.controller;

import com.java.koncert.model.Karta;
import com.java.koncert.model.Koncert;
import com.java.koncert.model.Zona;
import com.java.koncert.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class KartaRestController {
    KartaService kartaService;

    ZonaService zonaService;
    KorisnikService korisnikService;
    RezervacijaService rezervacijaService;

    public KartaRestController(KartaService kartaService, ZonaService zonaService, KorisnikService korisnikService, RezervacijaService rezervacijaService) {
        this.kartaService = kartaService;
        this.zonaService = zonaService;
        this.korisnikService = korisnikService;
        this.rezervacijaService = rezervacijaService;
    }

    @GetMapping("/karte")
    public List<Karta> list() {
        return kartaService.findAll();
    }

    @GetMapping("/karte/{id}")
    public Karta get(@PathVariable("id") int id) {
        Karta k = kartaService.findById(id);

        if (k == null) {
            throw new RuntimeException("karta id not found - " + id);}
        return k;
    }


    @PostMapping(value = "/karte",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody Karta k) throws Exception{
        try{
            kartaService.save(k);}
        catch(Exception e){
            e.printStackTrace();
        }

    }

}
