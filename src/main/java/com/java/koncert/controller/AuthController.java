package com.java.koncert.controller;

import com.java.koncert.model.*;
import com.java.koncert.responseAndRequestClass.RequestRezervacijaZona;
import com.java.koncert.responseAndRequestClass.ResponseRezervacijaKarte;
import com.java.koncert.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
public class AuthController {
    RezervacijaService rezervacijaService;
    ZonaService zonaService;
    KartaService kartaService;
    KoncertService koncertService;
    KorisnikService korisnikService;
    PromokodService promokodService;
    Logger logger =  LoggerFactory.getLogger(AuthController.class);

    public AuthController(RezervacijaService rezervacijaService, ZonaService zonaService, KartaService kartaService, KoncertService koncertService, KorisnikService korisnikService, PromokodService promokodService) {
        this.rezervacijaService = rezervacijaService;
        this.zonaService = zonaService;
        this.kartaService = kartaService;
        this.koncertService = koncertService;
        this.korisnikService = korisnikService;
        this.promokodService = promokodService;
    }
    @GetMapping("/rezervacija/{token}/{email}")
    public ResponseRezervacijaKarte findRezervacija(@PathVariable("token") String token, @PathVariable("email") String email) {
        try {
            Korisnik korisnik = korisnikService.findByEmail(email);
            Rezervacija rezervacija = rezervacijaService.findByTokenAndKorisnik(korisnik, token);
            //za tu rezervaciju treba da nadjemo sve karte
            List<Karta> karte = new ArrayList<>();
            List<Karta> sveKarte = kartaService.findAll();
            Zona z=null;
            for (Karta k : sveKarte) {
                if (k.getRezervacija()!=null){
                if (k.getRezervacija().equals(rezervacija)) {
                    //posto su sve karte iz iste zone
                    karte.add(k);

                }}

            }
            if (!karte.isEmpty()){
                Karta k=karte.get(0);
                z=  k.getZona();}

            ResponseRezervacijaKarte response=new ResponseRezervacijaKarte(rezervacija,karte,z);
            System.out.println(response);

            return response;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //brisanje rezervacije
    @DeleteMapping("/rezervacija/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable("id") int id) {
        try {
            Rezervacija rezervacija=rezervacijaService.findById(id);

            uvecajBrojKarti(rezervacija);
            rezervacijaService.deleteById(id);
            System.out.println("obrisana je rezervacija");
        } catch (EmptyResultDataAccessException e) {
            System.out.println("odabran id rezervacije ne postoji");

        }    }

    private void uvecajBrojKarti(Rezervacija rezervacija) {
        int brojKarata=rezervacija.getBrojKarata();
        List<Karta> karte=kartaService.findAll();
        Zona zona=null;
        Koncert koncert=null;
        for (Karta k:karte) {
            if (k.getRezervacija()!=null){
                if (k.getRezervacija().getIdrezervacija()==rezervacija.getIdrezervacija()){
zona=k.getZona();
koncert=zona.getKoncert();
                }
            }

        }
        //sad imamo zonu
        try {
            if (zona.getPreostaoBrKarata()!=zona.getKapacitet() ){
                zona.setPreostaoBrKarata(zona.getPreostaoBrKarata()+brojKarata);
            }
            zona.getZonaPK().setIdzona(zona.getZonaPK().getIdzona());
            zona.setKoncert(koncert);
            zonaService.save(zona);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    //
    @PutMapping(path="/rezervacija")
    public void putRezervacija(@RequestBody RequestRezervacijaZona rezervacijaZona) {
Rezervacija rezervacija=rezervacijaZona.getRezervacija();
Zona zona=rezervacijaZona.getZona();
Koncert koncert=null;
Rezervacija rezervacijaPreIzmene=rezervacijaService.findById(rezervacija.getIdrezervacija());
        if (zona!=null){
            koncert=zona.getKoncert();
        }
        pravljenjeKarti(rezervacija, zona, koncert, rezervacijaPreIzmene);


        if(rezervacija!=null){
            if (rezervacija.getKorisnik()!= null) {
                rezervacija.setKorisnik(rezervacija.getKorisnik());
            }
            if (rezervacija.getBrojKarata()!=0) {
                rezervacija.setBrojKarata(rezervacija.getBrojKarata());
            }
            if (rezervacija.getUkupno()!=0) {
                rezervacija.setUkupno(rezervacija.getUkupno());
            }
            if (rezervacija.getToken()!=null) {
               rezervacija.setToken(rezervacija.getToken());
            }
        }
        rezervacijaService.update(rezervacija);

    }

    private void pravljenjeKarti(Rezervacija rezervacija, Zona zona, Koncert koncert, Rezervacija rezervacijaPreIzmene) {
        int noviBrojKarti= rezervacija.getBrojKarata();
        int stariBrojKarata= rezervacijaPreIzmene.getBrojKarata();
        if (rezervacija != null && rezervacijaPreIzmene != null && zona !=null && koncert !=null) {
            for(int i=0; i<noviBrojKarti-stariBrojKarata;i++)  {
                Karta k=new Karta(-1, zona, koncert, rezervacija);
                kartaService.save(k);
            }}
        umanjiBrojSlobodnihKarti(noviBrojKarti-stariBrojKarata,zona,koncert);
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
}
