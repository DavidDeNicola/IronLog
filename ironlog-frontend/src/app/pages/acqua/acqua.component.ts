import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AcquaService } from '../../services/acqua.service';
import { ConsumoAcqua } from '../../models/nutrizione.model';

@Component({
  selector: 'app-acqua',
  imports: [FormsModule],
  templateUrl: './acqua.component.html',
  styleUrl: './acqua.component.scss'
})
export class AcquaComponent implements OnInit {

  data = new Date().toISOString().substring(0, 10);
  consumi: ConsumoAcqua[] = [];

  caricamento = true;
  errore = false;

  registrazioneInCorso = false;
  erroreRegistrazione: string | null = null;

  mlLiberi: number | null = null;
  obiettivo: number | null = 2000;

  constructor(private acquaService: AcquaService) {}

  ngOnInit(): void {
    this.carica();
  }

  get totaleConsumato(): number {
    return this.consumi.reduce((somma, c) => somma + c.mlConsumati, 0);
  }

  get percentuale(): number {
    if (!this.obiettivo || this.obiettivo <= 0) {
      return 0;
    }
    return Math.min(100, Math.round((this.totaleConsumato / this.obiettivo) * 100));
  }

  cambiaData(): void {
    this.carica();
  }

  aggiungiRapido(ml: number): void {
    this.registra(ml);
  }

  aggiungiLibero(): void {
    if (!this.mlLiberi || this.mlLiberi <= 0) {
      return;
    }
    this.registra(this.mlLiberi);
    this.mlLiberi = null;
  }

  private registra(ml: number): void {
    this.registrazioneInCorso = true;
    this.erroreRegistrazione = null;

    this.acquaService.registra({
      data: this.data,
      mlConsumati: ml,
      mlObiettivo: this.obiettivo
    }).subscribe({
      next: (consumo) => {
        this.consumi = [...this.consumi, consumo];
        if (consumo.mlObiettivo) {
          this.obiettivo = consumo.mlObiettivo;
        }
        this.registrazioneInCorso = false;
      },
      error: () => {
        this.registrazioneInCorso = false;
        this.erroreRegistrazione = 'Impossibile registrare il consumo d\'acqua. Riprova.';
      }
    });
  }

  private carica(): void {
    this.caricamento = true;
    this.errore = false;

    this.acquaService.getConsumi(this.data).subscribe({
      next: (consumi) => {
        this.consumi = consumi;
        const ultimo = consumi[consumi.length - 1];
        if (ultimo?.mlObiettivo) {
          this.obiettivo = ultimo.mlObiettivo;
        }
        this.caricamento = false;
      },
      error: () => {
        this.errore = true;
        this.caricamento = false;
      }
    });
  }
}
