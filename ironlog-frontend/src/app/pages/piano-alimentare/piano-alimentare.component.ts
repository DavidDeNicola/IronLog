import { Component, OnInit } from '@angular/core';
import { DatePipe, NgClass } from '@angular/common';

import { FaseService } from '../../services/fase.service';
import { PianoAlimentareService } from '../../services/piano-alimentare.service';
import { Fase, GruppoPasto, PianoAlimentare, PianoAlimentareSintesi, TipoPasto } from '../../models/nutrizione.model';

const ORDINE_PASTI: { tipoPasto: TipoPasto; etichetta: string; icona: string }[] = [
  { tipoPasto: 'COLAZIONE', etichetta: 'Colazione', icona: 'bi-sunrise' },
  { tipoPasto: 'PRANZO', etichetta: 'Pranzo', icona: 'bi-brightness-high' },
  { tipoPasto: 'SPUNTINO', etichetta: 'Spuntino', icona: 'bi-apple' },
  { tipoPasto: 'CENA', etichetta: 'Cena', icona: 'bi-moon-stars' }
];

@Component({
  selector: 'app-piano-alimentare',
  imports: [DatePipe, NgClass],
  templateUrl: './piano-alimentare.component.html',
  styleUrl: './piano-alimentare.component.scss'
})
export class PianoAlimentareComponent implements OnInit {

  faseAttiva: Fase | null = null;
  piani: PianoAlimentareSintesi[] = [];
  pianoSelezionato: PianoAlimentare | null = null;
  gruppiPasto: GruppoPasto[] = [];

  caricamento = true;
  errore = false;

  generazioneInCorso = false;
  erroreGenerazione: string | null = null;

  caricamentoDettaglio = false;

  constructor(
    private faseService: FaseService,
    private pianoAlimentareService: PianoAlimentareService
  ) {}

  ngOnInit(): void {
    this.caricamento = true;
    this.errore = false;

    this.faseService.getFaseAttiva().subscribe({
      next: (fase) => {
        this.faseAttiva = fase;
        this.caricaPiani();
      },
      error: () => {
        this.faseAttiva = null;
        this.caricamento = false;
      }
    });
  }

  genera(): void {
    if (!this.faseAttiva) {
      return;
    }
    this.generazioneInCorso = true;
    this.erroreGenerazione = null;

    this.pianoAlimentareService.genera(this.faseAttiva.id).subscribe({
      next: (piano) => {
        this.generazioneInCorso = false;
        this.pianoSelezionato = piano;
        this.gruppiPasto = this.raggruppaPerPasto(piano);
        this.caricaPiani();
      },
      error: () => {
        this.generazioneInCorso = false;
        this.erroreGenerazione = 'Impossibile generare il piano alimentare. Riprova.';
      }
    });
  }

  selezionaPiano(sintesi: PianoAlimentareSintesi): void {
    this.caricamentoDettaglio = true;
    this.pianoAlimentareService.getPiano(sintesi.id).subscribe({
      next: (piano) => {
        this.pianoSelezionato = piano;
        this.gruppiPasto = this.raggruppaPerPasto(piano);
        this.caricamentoDettaglio = false;
      },
      error: () => {
        this.caricamentoDettaglio = false;
      }
    });
  }

  private raggruppaPerPasto(piano: PianoAlimentare): GruppoPasto[] {
    return ORDINE_PASTI
      .map(({ tipoPasto, etichetta, icona }) => {
        const voci = piano.voci.filter((v) => v.tipoPasto === tipoPasto);
        const calorieTotali = voci.reduce((somma, v) => somma + v.calorie, 0);
        return { tipoPasto, etichetta, icona, voci, calorieTotali };
      })
      .filter((gruppo) => gruppo.voci.length > 0);
  }

  private caricaPiani(): void {
    if (!this.faseAttiva) {
      this.caricamento = false;
      return;
    }
    this.pianoAlimentareService.getPiani(this.faseAttiva.id).subscribe({
      next: (piani) => {
        this.piani = piani;
        this.caricamento = false;
      },
      error: () => {
        this.errore = true;
        this.caricamento = false;
      }
    });
  }
}
