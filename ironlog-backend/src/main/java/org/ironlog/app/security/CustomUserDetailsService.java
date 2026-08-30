package org.ironlog.app.security;

import lombok.RequiredArgsConstructor;
import org.ironlog.app.repository.UtenteRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UtenteRepository utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return utenteRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + username));
    }
}
