import { Component, EventEmitter, Output } from '@angular/core';
import { RouterLink, RouterLinkActive, Router } from '@angular/router';

import { AuthService } from '../../services/auth.service';
import {NgClass} from '@angular/common';

interface VoceMenu {
  etichetta: string;
  percorso: string;
  icona: string;
}

@Component({
  selector: 'app-sidebar',
  imports: [RouterLink, RouterLinkActive, NgClass],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {

  @Output() voceSelezionata = new EventEmitter<void>();

  voci: VoceMenu[] = [
    { etichetta: 'Dashboard',    percorso: '/dashboard',    icona: 'bi-grid-1x2' },
    { etichetta: 'Schede',       percorso: '/schede',       icona: 'bi-journal-text' },
    { etichetta: 'Allenamenti',  percorso: '/allenamento',  icona: 'bi-lightning-charge' },
    { etichetta: 'Esercizi',     percorso: '/esercizi',     icona: 'bi-list-check' },
    { etichetta: 'Statistiche',  percorso: '/statistiche',  icona: 'bi-bar-chart' },
    { etichetta: 'Storico',      percorso: '/storico',      icona: 'bi-clock-history' },
    { etichetta: 'Profilo',      percorso: '/profilo',      icona: 'bi-person' },
    { etichetta: 'Impostazioni', percorso: '/impostazioni', icona: 'bi-gear' }
  ];

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  get nomeUtente(): string {
    return this.authService.getUtente()?.sub ?? '';
  }

  get ruoloUtente(): string {
    return this.authService.getRuolo() === 'COACH' ? 'Coach' : 'Atleta';
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  onVoceClick(): void {
    this.voceSelezionata.emit();
  }
}
