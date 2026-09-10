package org.ironlog.app.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver resolver;

    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        boolean giaAutenticato = securityContext.getAuthentication() != null;
        boolean daValidare = authHeader != null && authHeader.startsWith("Bearer ") && !giaAutenticato;

        if (daValidare) {
            try {
                String token = authHeader.substring(7);
                String email = jwtService.getSubject(token);

                UserDetails utente = userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(utente, null, utente.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(authToken);

            } catch (Exception e) {
                // Token scaduto, malformato o firma non valida: il resolver scrive
                // la risposta di errore e la catena si ferma qui. Proseguire
                // produrrebbe una seconda risposta sulla stessa richiesta.
                resolver.resolveException(request, response, null, e);
                return;
            }
        }

        // Solo la validazione del token e' racchiusa nel try: le eccezioni sollevate
        // piu' avanti nella catena restano di competenza del GlobalExceptionHandler.
        filterChain.doFilter(request, response);
    }
}
