package com.java.koncert.service;

import com.java.koncert.security.CustomUserDetails;
import com.java.koncert.model.Korisnik;
import com.java.koncert.repository.KorisnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    KorisnikRepository korisnikRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final Korisnik korisnik = korisnikRepository.findByEmail(email);
        if (korisnik == null) {throw new UsernameNotFoundException("Korisnik '" + korisnik.getIme()+" "+korisnik.getPrezime() + "' nije pronadjen");
        }
        return new CustomUserDetails(korisnik);
    }
}
