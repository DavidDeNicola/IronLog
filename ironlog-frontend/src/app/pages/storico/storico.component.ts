import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';

import { SessioneService } from '../../services/sessione.service';
import { SessioneSintesi } from '../../models/sessione.model';

@Component({
  selector: 'app-storico',
  imports: [DatePipe],
  templateUrl: './storico.component.html',
  styleUrl: './storico.component.scss'
})
export class StoricoComponent implements OnInit {

  sessioni: SessioneSintesi[] = [];
  caricamento = true;
  errore = false;

  constructor(private sessioneService: SessioneService) {}

  ngOnInit(): void {
    this.sessioneService.getStorico().subscribe({
      next: (sessioni) => {
        this.sessioni = sessioni;
        this.caricamento = false;
      },
      error: () => {
        this.errore = true;
        this.caricamento = false;
      }
    });
  }

  durataMinuti(sessione: SessioneSintesi): number | null {
    if (!sessione.conclusaIl) {
      return null;
    }
    const inizio = new Date(sessione.eseguitaIl).getTime();
    const fine = new Date(sessione.conclusaIl).getTime();
    return Math.round((fine - inizio) / 60000);
  }
}
