package com.java.koncert.controller;

import com.java.koncert.model.Koncert;
import com.java.koncert.model.Korisnik;
import com.java.koncert.model.Rezervacija;
import com.java.koncert.model.Zona;
import com.java.koncert.service.KorisnikService;
import com.java.koncert.service.RezervacijaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//moraces da dodas cross-origin
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class KorisnikRestController {
KorisnikService korisnikService;
RezervacijaService rezervacijaService;

    public KorisnikRestController(KorisnikService korisnikService, RezervacijaService rezervacijaService) {
        this.korisnikService = korisnikService;
        this.rezervacijaService = rezervacijaService;
    }

    private final Logger log = LoggerFactory.getLogger(KorisnikRestController.class);
    @GetMapping("/korisnici")
    public List<Korisnik> listUsers() {

        List<Korisnik> lis=korisnikService.findAll();
        for (Korisnik k: lis) {
         //   log.info("Get call have been received at user/get/ "+k.getIme());
        }

        return lis;

    }
    @GetMapping("/korisnici/{id}")
    public Korisnik get(@PathVariable("id") int id) {
        Korisnik k = korisnikService.findById(id);
        if (k == null) {
            throw new RuntimeException("korisnik id not found - " + id);
        }
        return k;
    }
    @PostMapping(value = "/korisnici",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody Korisnik k) throws Exception{
        try{
            korisnikService.save(k);}
        catch(Exception e){
            e.printStackTrace();
        }

    }
    //get sve rezervacije za korisnika
    @GetMapping("/korisnik/{id}/rezervacije")
    public List<Rezervacija> getRezervacijeForKorisnik(@PathVariable("id") int id) {
        Korisnik k=korisnikService.findById(id);
        System.out.println(k);
        List<Rezervacija> lista=rezervacijaService.findAll();
        List<Rezervacija> potrebnaLista=rezervacijaService.findAll();
        for (Rezervacija r:lista) {
           Korisnik trenutni= r.getKorisnik();
           if(trenutni.equals(k)) {
                potrebnaLista.add(r);}}
        for (Rezervacija rez:potrebnaLista) {
            System.out.println(rez);
        }
        return potrebnaLista;
        }


}
