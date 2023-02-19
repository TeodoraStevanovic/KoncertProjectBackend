package com.java.koncert.controller;
import com.java.koncert.model.Rezervacija;
import com.java.koncert.service.RezervacijaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class RezervacijaRestController {
RezervacijaService rezervacijaService;
    Logger logger =  LoggerFactory.getLogger(RezervacijaRestController.class);
    public RezervacijaRestController(RezervacijaService rezervacijaService) {
        this.rezervacijaService = rezervacijaService;
    }

    @PostMapping(value = "/rezervacije")
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody Rezervacija rezervacija) {
        logger.debug(String.valueOf(rezervacija));
         rezervacijaService.save(rezervacija);
        logger.debug(String.valueOf(rezervacija));

    }

    @GetMapping("/rezervacije")
    public List<Rezervacija> listRez() {
                List<Rezervacija> rez=rezervacijaService.findAll();

        for (Rezervacija r:rez
             ) {
            System.out.println(r);
        }
                return rez;
    }


}

