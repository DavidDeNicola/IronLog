import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SchedaResponse, GiornoScheda } from '../../models/scheda.model';

import { SessioneService } from '../../services/sessione.service';
import { SchedaService } from '../../services/scheda.service';
import { ProssimoAllenamento, SessioneResponse } from '../../models/sessione.model';
import { SchedaSintesi } from '../../models/scheda.model';

@Component({
  selector: 'app-allenamento',
  imports: [],
  templateUrl: './allenamento.component.html',
  styleUrl: './allenamento.component.scss'
})
export class AllenamentoComponent implements OnInit {

  caricamento = true;
  errore = false;

  sessioneAperta: SessioneResponse | null = null;
  prossimo: ProssimoAllenamento | null = null;
  schede: SchedaSintesi[] = [];

  schedaSelezionata: SchedaResponse | null = null;
  caricamentoGiorni = false;

  constructor(
    private sessioneService: SessioneService,
    private schedaService: SchedaService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.sessioneService.getAperta().subscribe({
      next: (sessione) => {
        if (sessione) {
          this.sessioneAperta = sessione;
          this.caricamento = false;
        } else {
          this.caricaStatoRiposo();
        }
      },
      error: () => {
        this.errore = true;
        this.caricamento = false;
      }
    });
  }

  private caricaStatoRiposo(): void {
    this.sessioneService.getProssimo().subscribe({
      next: (prossimo) => {
        this.prossimo = prossimo;
      },
      error: () => {}
    });

    this.schedaService.getSchede().subscribe({
      next: (schede) => {
        this.schede = schede;
        this.caricamento = false;
      },
      error: () => {
        this.errore = true;
        this.caricamento = false;
      }
    });
  }

  selezionaScheda(id: number): void {
    this.caricamentoGiorni = true;
    this.schedaSelezionata = null;
    this.schedaService.getScheda(id).subscribe({
      next: (scheda) => {
        this.schedaSelezionata = scheda;
        this.caricamentoGiorni = false;
      },
      error: () => {
        this.caricamentoGiorni = false;
      }
    });
  }

  avvia(giornoSchedaId: number): void {
    this.router.navigate(['/allenamento/sessione'], {
      queryParams: { giorno: giornoSchedaId }
    });
  }
}
