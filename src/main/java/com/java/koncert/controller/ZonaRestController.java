package com.java.koncert.controller;

import com.java.koncert.model.Koncert;
import com.java.koncert.model.Zona;
import com.java.koncert.service.KoncertService;
import com.java.koncert.service.ZonaService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class ZonaRestController {
    ZonaService zonaService;
    KoncertService koncertService;

    public ZonaRestController(ZonaService zonaService, KoncertService koncertService) {
        this.zonaService = zonaService;
        this.koncertService = koncertService;
    }


    @GetMapping("/zone")
    public List<Zona> listZone() {
        return zonaService.findAll();
    }

    @GetMapping("/zone/{id}")
    public Zona getZona(@PathVariable("id") int id) {
        Zona k = zonaService.findById(id);
        if (k == null) {
            throw new RuntimeException("zona id not found - " + id);
        }
        return k;
    }
    @GetMapping("/koncerti/{id}/zone")
    public List<Zona> getZoneForKoncert(@PathVariable("id") int id) {
        Koncert koncert=koncertService.findById(id);
     //   System.out.println(koncert);
   List<Zona> sveZone=zonaService.findAll();

        List<Zona>potrebneZone=new ArrayList<>();
        for (Zona z: sveZone) {
            if (z.getKoncert().equals(koncert))
            {if(z.getPreostaoBrKarata()!=0){
               // System.out.println(z.getPreostaoBrKarata());
                potrebneZone.add(z);}}
        }
       return potrebneZone;
    }
    //broj slobodnih karata u zoni
    @GetMapping("/zone/{id}/slobodnaMesta")
    public int getBrojSlobodnihMesta(@PathVariable("id") int id) {
        System.out.println(id);
        Zona k = zonaService.findById(id);
        if (k == null) {
            throw new RuntimeException("zona id not found - " + id);
        }
        int broj=k.getPreostaoBrKarata();
        return broj;
    }

    //get koncert za odredjenu zonu



}
