package com.java.koncert.controller;
import com.java.koncert.model.Promokod;
import com.java.koncert.service.KorisnikService;
import com.java.koncert.service.PromokodService;
import com.java.koncert.service.RezervacijaService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class PromokodRestController {
PromokodService promokodService;
KorisnikService korisnikService;
RezervacijaService rezervacijaService;

    public PromokodRestController(PromokodService promokodService, KorisnikService korisnikService, RezervacijaService rezervacijaService) {
        this.promokodService = promokodService;
        this.korisnikService = korisnikService;
        this.rezervacijaService = rezervacijaService;
    }

    @GetMapping("/promokodovi")
    public List<Promokod> list() {
        return promokodService.findAll();
    }

    @GetMapping("/promokodovi/{id}")
    public Promokod get(@PathVariable("id") int id) {
        Promokod k = promokodService.findById(id);
        if (k == null) {
            throw new RuntimeException("promokod id not found - " + id);
        }
        return k;
    }

    @PostMapping(value = "/promokodovi",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody Promokod k) throws Exception{
        try{
            promokodService.save(k);}
        catch(Exception e){
            e.printStackTrace();
        }

    }

    @PatchMapping(path="/promokod/{id}", consumes="application/json")
    public Promokod patchMovie(@PathVariable("id") int id, @RequestBody Promokod patch) {

        Promokod promokod = promokodService.findById(id);
        if (patch.getKod()!= null) {
            promokod.setKod(patch.getKod());
        }
        if (patch.getIskoriscen()==0) {
            promokod.setIskoriscen(1);
        }else {promokod.setIskoriscen(1);}
        if (patch.getUpotrebljiv()!=0) {
            promokod.setUpotrebljiv(0);
        }else {promokod.setUpotrebljiv(0);}
        if (patch.getKorisnik()!= null) {
            promokod.setKorisnik(patch.getKorisnik());
        }
        if (patch.getRezervacija()!= null) {
            promokod.setRezervacija(patch.getRezervacija());
        }
        return promokodService.update(promokod);
    }

    @DeleteMapping("/promokodovi/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable("id") int id) {
        try {
            promokodService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("odabran id promokoda ne postoji");

        }    }

    //treba mi jos funkcija za generisanje promokoda kad neko napravi rezervaciju
}
