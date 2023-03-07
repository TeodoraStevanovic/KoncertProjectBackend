package com.java.koncert.security;
import com.java.koncert.model.Korisnik;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails{
    private Korisnik korisnik;

    public CustomUserDetails(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
//"ROLE_USER"
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    @Override
    public String getPassword() {
        //ovo mora da se menja ako dodajemo login sa passwordom
        //bcrypt za lozinka
        return "$2a$12$WG54ZvLA4x7Bjpk9DBtrmOtQnzcJEtn/yBcoT40i/EOtDWw2T3WDa";
    }

    @Override
    public String getUsername() {
        return korisnik.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    @Override
    public String toString() {
        return "CustomUserDetails{" +
                "korisnik=" + korisnik +
                '}';
    }
}
