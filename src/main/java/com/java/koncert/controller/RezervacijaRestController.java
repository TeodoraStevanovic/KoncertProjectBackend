package com.java.koncert.controller;
import com.java.koncert.model.*;
import com.java.koncert.service.KartaService;
import com.java.koncert.service.KoncertService;
import com.java.koncert.service.RezervacijaService;
import com.java.koncert.service.ZonaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class RezervacijaRestController {
RezervacijaService rezervacijaService;
ZonaService zonaService;
KartaService kartaService;
KoncertService koncertService;
    Logger logger =  LoggerFactory.getLogger(RezervacijaRestController.class);

    public RezervacijaRestController(RezervacijaService rezervacijaService, ZonaService zonaService, KartaService kartaService, KoncertService koncertService) {
        this.rezervacijaService = rezervacijaService;
        this.zonaService = zonaService;
        this.kartaService = kartaService;
        this.koncertService = koncertService;
    }

    @PostMapping(value = "/rezervacije")
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody Rezervacija rezervacija) {
        logger.info(String.valueOf(rezervacija));
         rezervacijaService.save(rezervacija);
        logger.debug(String.valueOf(rezervacija));

    }
    @PostMapping(value = "/rezervacije/{selectedZona}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addReservationAndCards(@RequestBody Rezervacija rezervacija,@PathVariable("selectedZona") int id) {
        logger.info(String.valueOf(rezervacija));
        Zona zona = zonaService.findById(id);
        if (zona == null) {
            throw new RuntimeException("zona id not found - " + id);
        }
        Koncert koncert=null;
        if (zona!=null){
            koncert=zona.getKoncert();
            //koncert=koncertService.findById(koncertid);
        }
        try {//treba da vidimo da li treba da se pravi novi korisnik ili ne




           Rezervacija rez= rezervacijaService.create(rezervacija);
           int brojKarata=rez.getBrojKarata();

            if (rez != null && zona!=null && koncert!=null) {
              for(int i=0; i<rez.getBrojKarata();i++)  {
                  Karta k=new Karta(-1,zona,koncert,rez);
                  kartaService.save(k);
              }}
            umanjiBrojSlobodnihKarti(brojKarata,zona,koncert);
            //generisanje promo koda
        }catch(Exception e){
            System.out.println("ovde jeee ex");
            e.printStackTrace();
        }

    }


    private void umanjiBrojSlobodnihKarti(int brojKarata, Zona zona,Koncert koncert) {
        try {
            if (zona.getPreostaoBrKarata()!=0 ){
                zona.setPreostaoBrKarata(zona.getPreostaoBrKarata()-brojKarata);
            }
            zona.setIdzona(zona.getIdzona());
            zona.setKoncert(koncert);
            zonaService.save(zona);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @GetMapping("/rezervacije")
    public List<Rezervacija> listRez() {
        return rezervacijaService.findAll();
    }


}

