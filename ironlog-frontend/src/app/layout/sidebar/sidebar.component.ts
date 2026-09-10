import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { RouterLink, RouterLinkActive, Router } from '@angular/router';

import { AuthService } from '../../services/auth.service';
import { ProfiloService } from '../../services/profilo.service';
import { ThemeService } from '../../services/theme.service';
import { NgClass } from '@angular/common';

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
export class SidebarComponent implements OnInit {

  @Output() voceSelezionata = new EventEmitter<void>();

  nomeCompleto = '';

  voci: VoceMenu[] = [
    { etichetta: 'Dashboard',    percorso: '/dashboard',    icona: 'bi-grid-1x2' },
    { etichetta: 'Schede',       percorso: '/schede',       icona: 'bi-journal-text' },
    { etichetta: 'Allenamenti',  percorso: '/allenamento',  icona: 'bi-lightning-charge' },
    { etichetta: 'Esercizi',     percorso: '/esercizi',     icona: 'bi-list-check' },
    { etichetta: 'Statistiche',  percorso: '/statistiche',  icona: 'bi-bar-chart' },
    { etichetta: 'Storico',      percorso: '/storico',      icona: 'bi-clock-history' },
    { etichetta: 'Profilo',      percorso: '/profilo',      icona: 'bi-person' }
  ];

  constructor(
    private authService: AuthService,
    private profiloService: ProfiloService,
    private themeService: ThemeService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.profiloService.getProfilo().subscribe({
      next: (profilo) => {
        this.nomeCompleto = `${profilo.nome} ${profilo.cognome}`;
      },
      error: () => {
        // se fallisce resta l'email come fallback
      }
    });
  }

  get emailUtente(): string {
    return this.authService.getUtente()?.sub ?? '';
  }

  get ruoloUtente(): string {
    return this.authService.getRuolo() === 'COACH' ? 'Coach' : 'Atleta';
  }

  get iniziali(): string {
    if (!this.nomeCompleto) {
      return '';
    }
    const parti = this.nomeCompleto.trim().split(' ');
    const prima = parti[0].charAt(0);
    const seconda = parti.length > 1 ? parti[parti.length - 1].charAt(0) : '';
    return (prima + seconda).toUpperCase();
  }

  get temaChiaro(): boolean {
    return this.themeService.temaChiaro;
  }

  alternaTema(): void {
    this.themeService.alterna();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  onVoceClick(): void {
    this.voceSelezionata.emit();
  }
}
