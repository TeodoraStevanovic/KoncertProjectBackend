package com.java.koncert.controller;
import com.java.koncert.jwt.JwtResponse;
import com.java.koncert.jwt.JwtTokenUtil;
import com.java.koncert.model.*;
import com.java.koncert.responseClass.ResponseRezervacijaKarte;
import com.java.koncert.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class RezervacijaRestController {
RezervacijaService rezervacijaService;
ZonaService zonaService;
KartaService kartaService;
KoncertService koncertService;
KorisnikService korisnikService;
PromokodService promokodService;
    Logger logger =  LoggerFactory.getLogger(RezervacijaRestController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailService userDetailsService;


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
    @PostMapping(value = "/rezervacije/{selectedZona}/{promokod}")
    @ResponseStatus(HttpStatus.CREATED)
    public List<String> addReservationAndCards(@RequestBody Rezervacija rezervacija,@PathVariable("selectedZona") int id,@PathVariable("promokod") String promokod) {
        List<String> list = new ArrayList<>();
        Zona zona = zonaService.findById(id);
        Koncert koncert=null;
        if (zona == null) {
            throw new RuntimeException("zona id not found - " + id);
        }

        if (zona!=null){
            koncert=zona.getKoncert();
        }
        try {
            Korisnik korisnikIzBaze = daLiVecPostojiKorisnik(rezervacija);
            if (korisnikIzBaze!=null) {
                rezervacija.setKorisnik(korisnikIzBaze);

            }else{
                Korisnik korisnik= rezervacija.getKorisnik();
                Korisnik sacuvanKorisnik= korisnikService.saveAndReturn(korisnik);
                rezervacija.setKorisnik(sacuvanKorisnik);
            }
            //generisanje tokena
            String token=createAuthenticationToken(rezervacija.getKorisnik().getEmail());
            rezervacija.setToken(token);
            logger.info(token+" u metodi");
            //cuvanje rezervacije
            Rezervacija rez= rezervacijaService.create(rezervacija);
            //pravljenje karti
            int brojKarata=rez.getBrojKarata();
            if (rez != null && zona!=null && koncert!=null) {
                for(int i=0; i<rez.getBrojKarata();i++)  {
                    Karta k=new Karta(-1,zona,koncert,rez);
                    kartaService.save(k);
                }}
            //smanjenje broja slobodnih mesta

            umanjiBrojSlobodnihKarti(brojKarata,zona,koncert);
            ponistiPrimenjeniPromokod(promokod);
            //generisanje novog promo koda
            String generisanPromokod=generisiNoviPromoKod(rez);
            logger.info(generisanPromokod+" u metodi");
            //ponistenje promokoda ako je primenjen

            list.add(token);
            list.add(generisanPromokod);


        }catch(Exception e){
            System.out.println("ovde jeee ex");
            e.printStackTrace();
        }
        return list;
    }
    @PostMapping(value = "/rezervacije/{selectedZona}")
    @ResponseStatus(HttpStatus.CREATED)
    public List<String> addReservationAndCardsBezPromokoda(@RequestBody Rezervacija rezervacija,@PathVariable("selectedZona") int id) {
        List<String> list = new ArrayList<>();
        Zona zona = zonaService.findById(id);
        Koncert koncert=null;
        if (zona == null) {
            throw new RuntimeException("zona id not found - " + id);
        }

        if (zona!=null){
            koncert=zona.getKoncert();
        }
        try {
            Korisnik korisnikIzBaze = daLiVecPostojiKorisnik(rezervacija);
            if (korisnikIzBaze!=null) {
                rezervacija.setKorisnik(korisnikIzBaze);

            }else{
                Korisnik korisnik= rezervacija.getKorisnik();
                Korisnik sacuvanKorisnik= korisnikService.saveAndReturn(korisnik);
                rezervacija.setKorisnik(sacuvanKorisnik);
            }
            //generisanje tokena
            String token=createAuthenticationToken(rezervacija.getKorisnik().getEmail());
            rezervacija.setToken(token);

            //cuvanje rezervacije
            Rezervacija rez= rezervacijaService.create(rezervacija);
            //pravljenje karti
            int brojKarata=rez.getBrojKarata();
            if (rez != null && zona!=null && koncert!=null) {
                for(int i=0; i<rez.getBrojKarata();i++)  {
                    Karta k=new Karta(-1,zona,koncert,rez);
                    kartaService.save(k);
                }}
            //smanjenje broja slobodnih mesta

            umanjiBrojSlobodnihKarti(brojKarata,zona,koncert);
            //generisanje novog promo koda
            String generisanPromokod=generisiNoviPromoKod(rez);
            //ponistenje promokoda ako je primenjen

            list.add(token);
            list.add(generisanPromokod);


        }catch(Exception e){
            System.out.println("ovde jeee ex");
            e.printStackTrace();
        }
        return list;
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

    private String generisiNoviPromoKod(Rezervacija rez) {
        //generisanje promo koda
        String promoKod= generatePromoCode();
        Promokod promokod=new Promokod(promoKod,0,1, rez.getKorisnik(), rez);
        promokodService.save(promokod);
        return promoKod;
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



    private void ponistiPrimenjeniPromokod(String promokod) {
        List<Promokod> promokodovi=promokodService.findAll();
Promokod ponistitikod=null;
        for (Promokod kod:promokodovi) {
            if(kod.getKod().equals(promokod)){
                ponistitikod=kod;
            }

        }
if(ponistitikod!=null){
    if (ponistitikod.getKod()!= null) {
        ponistitikod.setKod(ponistitikod.getKod());
    }
    if (ponistitikod.getIskoriscen()==0) {
        ponistitikod.setIskoriscen(1);
    }else {ponistitikod.setIskoriscen(1);}
    if (ponistitikod.getUpotrebljiv()!=0) {
        ponistitikod.setUpotrebljiv(0);
    }else {ponistitikod.setUpotrebljiv(0);}
    if (ponistitikod.getKorisnik()!= null) {
        ponistitikod.setKorisnik(ponistitikod.getKorisnik());
    }
    if (ponistitikod.getRezervacija()!= null) {
        ponistitikod.setRezervacija(ponistitikod.getRezervacija());
    }
}
         promokodService.update(ponistitikod);
    }
    //

    public String createAuthenticationToken(String email) throws Exception {
        authenticate(email);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        final String token = jwtTokenUtil.generateToken(userDetails);
        return token;
    }


    private Authentication authenticate(String email) throws Exception {
        try {
            Authentication auth= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, "lozinka"));
            return auth;
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    //
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
                if (k.getRezervacija().equals(rezervacija)) {
                    //posto su sve karte iz iste zone
                    karte.add(k);

                }

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


}

