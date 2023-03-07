package com.java.koncert.controller;

import com.java.koncert.model.Koncert;

import com.java.koncert.model.Zona;
import com.java.koncert.service.KoncertService;
import com.java.koncert.service.ZonaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class KoncertRestController {
ZonaService zonaService;
    KoncertService koncertService;

    public KoncertRestController(ZonaService zonaService, KoncertService koncertService) {
        this.zonaService = zonaService;
        this.koncertService = koncertService;
    }

    @GetMapping("/koncerti")
    public List<Koncert> listKoncerte() {
        return koncertService.findAll();
    }

    @GetMapping("/koncerti/{id}")
    public Koncert getKoncert(@PathVariable("id") int id) {
        Koncert k = koncertService.findById(id);

        if (k == null) {
            throw new RuntimeException("koncert id not found - " + id);
        }

        return k;
    }


    @PostMapping(value = "/koncerti",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody Koncert k) throws Exception{
try{
        koncertService.save(k);}
catch(Exception e){
    e.printStackTrace();
}

    }

    @GetMapping("/zone/{id}/koncert")
    public Koncert getKoncertForZona(@PathVariable("id") int id) {
        Zona z=zonaService.findById(id);
        System.out.println(z);
        Koncert potrebanKoncert=z.getKoncert();
       // Koncert potrebanKoncert=koncertService.findById(z.getId().getIdkoncert());
        return potrebanKoncert;
    }
}