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

import java.util.*;

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

    @PutMapping(path="/rezervacija/karte/dodavanje/{broj}")
    public void putRezervacija(@RequestBody RequestRezervacijaZona rezervacijaZona,@PathVariable("broj") int brojKarti) {
Rezervacija rezervacija=rezervacijaZona.getRezervacija();
Zona zona=rezervacijaZona.getZona();
Koncert koncert=null;
        if (zona!=null){
            koncert=zona.getKoncert();
        }

        pravljenjeKarti(rezervacija, zona, koncert,brojKarti);

       Rezervacija rez= izmeniRezervacijuUmanjiBrojKarti(brojKarti, rezervacija, zona);

        rezervacijaService.update(rez);



    }

    private Rezervacija izmeniRezervacijuUmanjiBrojKarti(int brojKarti, Rezervacija rezervacija, Zona zona) {
        boolean primenjenEarlyBird=false;
        boolean primenjenKupon=false;
        Map<String, Boolean> mapa= izracunajPopuste(zona, false, false, rezervacija);
        primenjenKupon=mapa.get("kupon");
        primenjenEarlyBird=mapa.get("earlyBird");

        double ukupnaCena=0;
        int noviBrojKarti= rezervacija.getBrojKarata()+ brojKarti;

        ukupnaCena = izracunajPonovoUkupnuCenu(zona, primenjenEarlyBird, primenjenKupon, ukupnaCena, noviBrojKarti);

        rezervacija.setUkupno(ukupnaCena);
        rezervacija.setBrojKarata(noviBrojKarti);


        if (rezervacija.getKorisnik()!= null) {
            rezervacija.setKorisnik(rezervacija.getKorisnik());
        }
        if (rezervacija.getToken()!=null) {
            rezervacija.setToken(rezervacija.getToken());
        }
        return rezervacija;
    }

    private void izmeniRezervacijuUmanjiBrojKarti(Rezervacija rezervacija, Zona zona, Koncert koncert, int brojKarti) {
        izmeniRezervacijuUmanjiBrojKarti(brojKarti, rezervacija, zona);

        rezervacijaService.update(rezervacija);
    }

    private void pravljenjeKarti(Rezervacija rezervacija, Zona zona, Koncert koncert,int noviBrojKarti) {
        if (rezervacija != null && zona !=null && koncert !=null) {
            for(int i=0; i<noviBrojKarti;i++)  {
                Karta k=new Karta(-1, zona, koncert, rezervacija);
                kartaService.save(k);
            }}
        umanjiBrojSlobodnihKarti(noviBrojKarti,zona,koncert);
    }



    @DeleteMapping("/rezervacija/karte/{karteZaBrisanje}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCard(@PathVariable("karteZaBrisanje")  String niz) {
// Convert the comma-separated IDs to an array of integers
        int[] nizKartaId = Arrays.stream(niz.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
        Karta karta=kartaService.findById(nizKartaId[0]);
        Rezervacija rezervacija=rezervacijaService.findById(karta.getRezervacija().getIdrezervacija());
        int brojKartiKojeSuObrisane=nizKartaId.length;
        Zona zona=karta.getZona();
        izmeniRezervaciju(rezervacija,brojKartiKojeSuObrisane,zona);
//sredi broj slobodnih karti
        uvecajBrojSlobodnihKarti(brojKartiKojeSuObrisane,zona,zona.getKoncert());
for (int id:nizKartaId){
    kartaService.deleteById(id);
    System.out.println("obrisana je karta: "+id);
}

        }
    private void uvecajBrojSlobodnihKarti(int brojKarata, Zona zona,Koncert koncert) {
        try {
            if (zona.getPreostaoBrKarata()!=zona.getKapacitet()&& zona.getPreostaoBrKarata()+brojKarata<=zona.getKapacitet()){
                zona.setPreostaoBrKarata(zona.getPreostaoBrKarata()+brojKarata);
            }
            zona.getZonaPK().setIdzona(zona.getZonaPK().getIdzona());
            zona.setKoncert(koncert);
            zonaService.save(zona);

        }catch (Exception ex){
            ex.printStackTrace();
        }
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
    private void izmeniRezervaciju(Rezervacija rezervacija, int brojKartiKojeSuObrisane,Zona zona) {
        boolean primenjenEarlyBird=false;
        boolean primenjenKupon=false;
        Map<String, Boolean> mapa= izracunajPopuste(zona, false, false,rezervacija);
        primenjenKupon=mapa.get("kupon");
        primenjenEarlyBird=mapa.get("earlyBird");

        double ukupnaCena=0;
        int noviBrojKarti=rezervacija.getBrojKarata()-brojKartiKojeSuObrisane;
        ukupnaCena = izracunajPonovoUkupnuCenu(zona, primenjenEarlyBird, primenjenKupon, ukupnaCena, noviBrojKarti);

        rezervacija.setUkupno(ukupnaCena);
        rezervacija.setBrojKarata(noviBrojKarti);

            if (rezervacija.getKorisnik()!= null) {
                rezervacija.setKorisnik(rezervacija.getKorisnik());
            }
            if (rezervacija.getToken()!=null) {
                rezervacija.setToken(rezervacija.getToken());
            }

        rezervacijaService.update(rezervacija);

    }

    private double izracunajPonovoUkupnuCenu(Zona zona, boolean primenjenEarlyBird, boolean primenjenKupon, double ukupnaCena, int noviBrojKarti) {
        if (noviBrojKarti > 4) {
            int[] karte = new int[noviBrojKarti];
            int[] karteUmanjenaCena;
            for (int i = 0; i < karte.length; i++) {
                karte[i] = zona.getCena();
            }
            karteUmanjenaCena = this.getEvery5th(karte);

            for (int i = 0; i < karteUmanjenaCena.length; i++) {

                ukupnaCena += karteUmanjenaCena[i];
            }
        } else {
            ukupnaCena = noviBrojKarti * zona.getCena();
        }
        //
        if (primenjenEarlyBird == true) {
            ukupnaCena = ukupnaCena * 0.9;
            System.out.println(ukupnaCena + " nako primenjenog early birda");
        }
        if (primenjenKupon == true) {
            ukupnaCena = ukupnaCena * 0.95;
            System.out.println(ukupnaCena + " nako primenjenog kupona");
        }
//sada su primenjeni svi popusti koji treba da se primene
        System.out.println(ukupnaCena + " na kraju metode");
//treba promeniti na rezervaciji ukupan broj karti i ukupnu cenu
        return ukupnaCena;
    }

    private Map<String, Boolean> izracunajPopuste(Zona zona, boolean primenjenEarlyBird, boolean primenjenKupon, Rezervacija rezervacija) {
        double prethodnaCenaBezPopusta=0;
        int brojKartiPreIzmene = rezervacija.getBrojKarata();
        if (brojKartiPreIzmene > 4) {
            int[] karte = new int[brojKartiPreIzmene];
            int[] karteUmanjenaCena;
            for (int i = 0; i < karte.length; i++) {
                karte[i] = zona.getCena();
            }
            karteUmanjenaCena = this.getEvery5th(karte);

            for (int i = 0; i < karteUmanjenaCena.length; i++) {
                prethodnaCenaBezPopusta += karteUmanjenaCena[i];
            }
        } else {
            prethodnaCenaBezPopusta = zona.getCena() * brojKartiPreIzmene;
        }


        double popust = prethodnaCenaBezPopusta - rezervacija.getUkupno();
        System.out.println(prethodnaCenaBezPopusta);
if (popust>0) {
    if ((prethodnaCenaBezPopusta / popust) != 10 && (prethodnaCenaBezPopusta / popust) != 5) {
        primenjenKupon = true;
        primenjenEarlyBird = true;
    } else {
        if (prethodnaCenaBezPopusta / popust == 10) {
            primenjenKupon = false;
            primenjenEarlyBird = true;
        } else {
            if (prethodnaCenaBezPopusta / popust == 5) {
                primenjenKupon = true;
                primenjenEarlyBird = false;
            }
        }
    }
}else{
    System.out.println("Nisu primenjeni ni early bird ni kupon popusti");
}
         Map<String,Boolean> mapa=new HashMap<>();
        mapa.put("kupon",primenjenKupon);
        mapa.put("earlyBird",primenjenEarlyBird);
        return mapa;
    }

    public int[] getEvery5th(int[] arr) {
        int[] result = Arrays.copyOf(arr, arr.length);

        for (int i = 0; i < arr.length; i++) {
            if ((i + 1) % 5 == 0) {
                result[i] = (int) Math.round(result[i] * 0.5);
            }
        }

        return result;
    }


}
