import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const authService = inject(AuthService);
  const router = inject(Router);

  const richiestaDiAutenticazione = req.url.includes('/auth/');
  const token = authService.getToken();

  const richiesta = (!token || richiestaDiAutenticazione)
    ? req
    : req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });

  return next(richiesta).pipe(
    catchError((errore: HttpErrorResponse) => {
      // Token scaduto o non piu' valido: si azzera la sessione e si torna al login.
      // Sulle chiamate di login/registrazione il 401 e' invece un esito atteso
      // e va lasciato gestire al componente.
      if (errore.status === 401 && !richiestaDiAutenticazione) {
        authService.logout();
        router.navigate(['/login']);
      }
      return throwError(() => errore);
    })
  );
};
