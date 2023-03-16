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
@RequestMapping("/api")
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

    @GetMapping(value = "/promokod/{kod}")
    public boolean check(@PathVariable("kod") String promokod) {
        System.out.println(promokod);
        try {
            List<Promokod> promokodovi = promokodService.findAll();
            Promokod trazeniPromokod = null;
            for (Promokod kod : promokodovi) {
                if (kod.getKod().equals(promokod)) {
                    trazeniPromokod = kod;

                }
            }

            if (trazeniPromokod != null) {
                if (trazeniPromokod.getIskoriscen() == 0 && trazeniPromokod.getUpotrebljiv() == 1) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        }catch (Exception e){e.printStackTrace();}
        return false;
   }


    @PutMapping(path="/promokod/{id}")
    public Promokod patchPromo(@PathVariable("id") int idRezervacije) {
        //prvo nadjemo promokod za rezervaciju
        Promokod promokod=pronadjiPromokod(idRezervacije);
        if(promokod!=null){
            if (promokod.getKod()!= null) {
                promokod.setKod(promokod.getKod());
            }
            if (promokod.getIskoriscen()==0) {
                promokod.setIskoriscen(1);
            }else {promokod.setIskoriscen(1);}
            if (promokod.getUpotrebljiv()!=0) {
                promokod.setUpotrebljiv(0);
            }else {promokod.setUpotrebljiv(0);}
            if (promokod.getKorisnik()!= null) {
                promokod.setKorisnik(promokod.getKorisnik());
            }
            if (promokod.getRezervacija()!= null) {
                promokod.setRezervacija(promokod.getRezervacija());
            }
        }
        return  promokodService.update(promokod);

    }

    private Promokod pronadjiPromokod(int id) {
        List<Promokod> sviPromokodovi=promokodService.findAll();
        Promokod promokod=null;
        for (Promokod p:sviPromokodovi) {
            if (p.getRezervacija() != null) {
                if (p.getRezervacija().getIdrezervacija() == id) {
                    promokod = p;
                }
            }
        }
        return promokod;
    }

}
