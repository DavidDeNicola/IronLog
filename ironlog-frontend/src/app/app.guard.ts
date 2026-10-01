import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { AuthService } from './services/auth.service';

export const authGuard: CanActivateFn = () => {

  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAutenticato()) {
    return true;
  }

  return router.createUrlTree(['/login']);
};

export const coachGuard: CanActivateFn = () => {

  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.getRuolo() === 'COACH') {
    return true;
  }

  return router.createUrlTree(['/dashboard']);
};

/**
 * La dashboard è solo per gli atleti: un coach che ci arriva (es. riaprendo
 * il sito con il token ancora salvato) viene portato alla sua pagina iniziale.
 */
export const dashboardGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.getRuolo() === 'COACH') {
    return router.createUrlTree(['/coach/clienti']);
  }
  return true;
};
