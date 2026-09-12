import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service'; // adatta il path/nome reale

interface VoceTabBar {
  etichetta: string;
  icona: string;
  link: string;
}

@Component({
  selector: 'app-tab-bar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './tab-bar.component.html',
  styleUrl: './tab-bar.component.scss'
})
export class TabBarComponent {
  private authService = inject(AuthService);

  private vociAtleta: VoceTabBar[] = [
    { etichetta: 'Dashboard', icona: 'bi-house', link: '/dashboard' },
    { etichetta: 'Allenamento', icona: 'bi-lightning', link: '/allenamento' },
    { etichetta: 'Storico', icona: 'bi-clock-history', link: '/storico' },
    { etichetta: 'Statistiche', icona: 'bi-bar-chart', link: '/statistiche' },
    { etichetta: 'Profilo', icona: 'bi-person', link: '/profilo' },
  ];

  private vociCoach: VoceTabBar[] = [
    { etichetta: 'Dashboard', icona: 'bi-house', link: '/dashboard' },
    { etichetta: 'Clienti', icona: 'bi-people', link: '/coach/clienti' },
    { etichetta: 'Statistiche', icona: 'bi-bar-chart', link: '/statistiche' },
    { etichetta: 'Profilo', icona: 'bi-person', link: '/profilo' },
  ];

  get voci(): VoceTabBar[] {
    return this.authService.getRuolo()== "COACH" ? this.vociCoach : this.vociAtleta;
  }
}
