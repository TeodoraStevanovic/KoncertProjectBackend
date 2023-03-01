package com.java.koncert.controller;
import com.java.koncert.model.*;
import com.java.koncert.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class RezervacijaRestController {
RezervacijaService rezervacijaService;
ZonaService zonaService;
KartaService kartaService;
KoncertService koncertService;
KorisnikService korisnikService;
PromokodService promokodService;
    Logger logger =  LoggerFactory.getLogger(RezervacijaRestController.class);


    public RezervacijaRestController(KorisnikService korisnikService,RezervacijaService rezervacijaService, ZonaService zonaService, KartaService kartaService, KoncertService koncertService, PromokodService promokodService) {
       this.korisnikService=korisnikService;
       this.rezervacijaService = rezervacijaService;
        this.zonaService = zonaService;
        this.kartaService = kartaService;
        this.koncertService = koncertService;
        this.promokodService = promokodService;
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
        }
        try {//treba da vidimo da li treba da se pravi novi korisnik ili ne
            Korisnik korisnikIzBaze = daLiVecPostojiKorisnik(rezervacija);
            if (korisnikIzBaze!=null) {
                rezervacija.setKorisnik(korisnikIzBaze);

            }else{
                Korisnik korisnik= rezervacija.getKorisnik();
                rezervacija.setKorisnik(korisnik);
                korisnikService.save(korisnik);
            }
           Rezervacija rez= rezervacijaService.create(rezervacija);
           int brojKarata=rez.getBrojKarata();
            if (rez != null && zona!=null && koncert!=null) {
              for(int i=0; i<rez.getBrojKarata();i++)  {
                  Karta k=new Karta(-1,zona,koncert,rez);
                  kartaService.save(k);
              }}
            umanjiBrojSlobodnihKarti(brojKarata,zona,koncert);
            generisiNoviPromoKod(rez);
        }catch(Exception e){
            System.out.println("ovde jeee ex");
            e.printStackTrace();
        }
    }

    private Korisnik daLiVecPostojiKorisnik(Rezervacija rezervacija) {
        Korisnik korisnik= rezervacija.getKorisnik();
        List<Korisnik> sviKornisnici=korisnikService.findAll();
        for (Korisnik k:sviKornisnici) {
            if (k.getEmail().equals(korisnik.getEmail())){
                return k;
            }
        }
        return null;
    }

    private void generisiNoviPromoKod(Rezervacija rez) {
        //generisanje promo koda
        String promoKod= generatePromoCode();
        logger.info(promoKod);
        Promokod promokod=new Promokod(promoKod,0,1, rez.getKorisnik(), rez);
        promokodService.save(promokod);
    }


    private void umanjiBrojSlobodnihKarti(int brojKarata, Zona zona,Koncert koncert) {
        try {
            if (zona.getPreostaoBrKarata()!=0 ){
                zona.setPreostaoBrKarata(zona.getPreostaoBrKarata()-brojKarata);
            }
            zona.getZonaPK().setIdzona(zona.getZonaPK().getIdzona());
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
    ////////////////////////////////////////////////////////////////
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 8;
    private static final int MAX_ATTEMPTS = 100;

    private static final SecureRandom random = new SecureRandom();

    public String generatePromoCode() {
        String code = null;
        int attempts = 0;
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < CODE_LENGTH; i++) {
                int index = random.nextInt(ALPHABET.length());
                sb.append(ALPHABET.charAt(index));
            }
            code = sb.toString();
            attempts++;
        } while (isCodeAlreadyUsed(code) && attempts < MAX_ATTEMPTS);

        if (attempts == MAX_ATTEMPTS) {
            throw new RuntimeException("Failed to generate a unique promo code after " + MAX_ATTEMPTS + " attempts");
        }

        return code;
    }

    private  boolean isCodeAlreadyUsed(String code) {
        List<Promokod> promokodovi=promokodService.findAll();

        for (Promokod kod:promokodovi) {
            if(kod.getKod().equals(code)){ return true;}

        }

        return false;
    }

}

