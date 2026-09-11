import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';

import { FaseService } from '../../services/fase.service';
import { Fase, FaseSintesi, FaseCreazione, TipoFase } from '../../models/nutrizione.model';

interface OpzioneTipo {
  valore: TipoFase;
  etichetta: string;
}

@Component({
  selector: 'app-fasi',
  imports: [FormsModule, DatePipe],
  templateUrl: './fasi.component.html',
  styleUrl: './fasi.component.scss'
})
export class FasiComponent implements OnInit {

  readonly tipi: OpzioneTipo[] = [
    { valore: 'BULK', etichetta: 'Bulk' },
    { valore: 'CUT', etichetta: 'Cut' },
    { valore: 'MAINTENANCE', etichetta: 'Mantenimento' },
    { valore: 'REVERSE_DIET', etichetta: 'Reverse diet' },
    { valore: 'MINI_CUT', etichetta: 'Mini cut' },
    { valore: 'DIET_BREAK', etichetta: 'Diet break' }
  ];

  faseAttiva: Fase | null = null;
  storico: FaseSintesi[] = [];

  caricamento = true;
  errore = false;

  form: FaseCreazione = {
    tipo: 'CUT',
    dataInizio: new Date().toISOString().substring(0, 10),
    targetCaloricoOverride: null,
    targetProteineGOverride: null,
    targetGrassiGOverride: null,
    targetCarboidratiGOverride: null
  };

  salvataggioInCorso = false;
  erroreSalvataggio: string | null = null;

  chiusuraInCorso = false;
  erroreChiusura: string | null = null;

  constructor(private faseService: FaseService) {}

  ngOnInit(): void {
    this.caricaTutto();
  }

  get badgeAdattato(): boolean {
    return !!this.faseAttiva && this.faseAttiva.targetCaloricoAttuale !== this.faseAttiva.baseTargetCalorico;
  }

  creaFase(): void {
    this.salvataggioInCorso = true;
    this.erroreSalvataggio = null;

    this.faseService.creaFase(this.form).subscribe({
      next: () => {
        this.salvataggioInCorso = false;
        this.resettaForm();
        this.caricaTutto();
      },
      error: () => {
        this.salvataggioInCorso = false;
        this.erroreSalvataggio = 'Impossibile creare la fase. Controlla i dati e riprova.';
      }
    });
  }

  chiudiFase(): void {
    if (!this.faseAttiva) {
      return;
    }
    this.chiusuraInCorso = true;
    this.erroreChiusura = null;

    this.faseService.chiudiFase(this.faseAttiva.id).subscribe({
      next: () => {
        this.chiusuraInCorso = false;
        this.caricaTutto();
      },
      error: () => {
        this.chiusuraInCorso = false;
        this.erroreChiusura = 'Impossibile chiudere la fase. Riprova più tardi.';
      }
    });
  }

  private resettaForm(): void {
    this.form = {
      tipo: 'CUT',
      dataInizio: new Date().toISOString().substring(0, 10),
      targetCaloricoOverride: null,
      targetProteineGOverride: null,
      targetGrassiGOverride: null,
      targetCarboidratiGOverride: null
    };
  }

  private caricaTutto(): void {
    this.caricamento = true;
    this.errore = false;

    this.faseService.getFasi().subscribe({
      next: (storico) => {
        this.storico = storico;
        this.caricamento = false;
      },
      error: () => {
        this.errore = true;
        this.caricamento = false;
      }
    });

    this.faseService.getFaseAttiva().subscribe({
      next: (attiva) => this.faseAttiva = attiva,
      error: () => this.faseAttiva = null
    });
  }
}
