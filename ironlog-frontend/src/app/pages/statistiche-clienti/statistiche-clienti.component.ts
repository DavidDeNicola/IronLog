import { Component, OnInit } from '@angular/core';
import { NgClass } from '@angular/common';

import { CoachService } from '../../services/coach.service';
import { StatisticaCliente } from '../../models/coach.model';

@Component({
  selector: 'app-statistiche-clienti',
  imports: [NgClass],
  templateUrl: './statistiche-clienti.component.html',
  styleUrl: './statistiche-clienti.component.scss'
})
export class StatisticheClientiComponent implements OnInit {

  clienti: StatisticaCliente[] = [];
  caricamento = true;
  errore = false;

  constructor(private coachService: CoachService) {}

  ngOnInit(): void {
    this.coachService.getStatisticheClienti().subscribe({
      next: (clienti) => {
        this.clienti = clienti;
        this.caricamento = false;
      },
      error: () => {
        this.errore = true;
        this.caricamento = false;
      }
    });
  }

  get totaleAllenamenti(): number {
    return this.clienti.reduce((somma, c) => somma + c.allenamentiUltimi30Giorni, 0);
  }

  iniziali(cliente: StatisticaCliente): string {
    return (cliente.nome.charAt(0) + cliente.cognome.charAt(0)).toUpperCase();
  }

  etichettaProgressione(p: string): string {
    switch (p) {
      case 'IN_PROGRESSO': return 'In progresso';
      case 'IN_CALO': return 'In calo';
      case 'STABILE': return 'Stabile';
      default: return 'Dati insuff.';
    }
  }

  iconaProgressione(p: string): string {
    switch (p) {
      case 'IN_PROGRESSO': return 'bi-arrow-up-right';
      case 'IN_CALO': return 'bi-arrow-down-right';
      case 'STABILE': return 'bi-dash-lg';
      default: return 'bi-question-lg';
    }
  }

  classeProgressione(p: string): string {
    switch (p) {
      case 'IN_PROGRESSO': return 'progresso';
      case 'IN_CALO': return 'calo';
      case 'STABILE': return 'stabile';
      default: return 'insufficiente';
    }
  }
}
