import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { forkJoin } from 'rxjs';

import { ProfiloMetabolicoService } from '../../services/profilo-metabolico.service';
import { ProfiloService } from '../../services/profilo.service';
import { ProfiloMetabolico, LivelloAttivita, FormulaMetabolica, Sesso } from '../../models/nutrizione.model';
import { Profilo } from '../../models/profilo.model';

interface OpzioneLivello {
  valore: LivelloAttivita;
  etichetta: string;
}

@Component({
  selector: 'app-profilo-metabolico',
  imports: [FormsModule, DatePipe],
  templateUrl: './profilo-metabolico.component.html',
  styleUrl: './profilo-metabolico.component.scss'
})
export class ProfiloMetabolicoComponent implements OnInit {

  readonly livelli: OpzioneLivello[] = [
    { valore: 'SEDENTARIO', etichetta: 'Sedentario' },
    { valore: 'LEGGERMENTE_ATTIVO', etichetta: 'Leggermente attivo' },
    { valore: 'MODERATAMENTE_ATTIVO', etichetta: 'Moderatamente attivo' },
    { valore: 'MOLTO_ATTIVO', etichetta: 'Molto attivo' },
    { valore: 'ESTREMAMENTE_ATTIVO', etichetta: 'Estremamente attivo' }
  ];

  profilo: Profilo | null = null;
  attuale: ProfiloMetabolico | null = null;
  storico: ProfiloMetabolico[] = [];

  caricamento = true;
  errore = false;

  formBiometrici: { sesso: Sesso | null; dataNascita: string } = { sesso: null, dataNascita: '' };
  salvataggioBiometriciInCorso = false;
  erroreBiometrici: string | null = null;

  formMetabolico: { livelloAttivita: LivelloAttivita | null; formulaForzata: FormulaMetabolica | null } = {
    livelloAttivita: null,
    formulaForzata: null
  };
  calcoloInCorso = false;
  erroreCalcolo: string | null = null;

  constructor(
    private profiloMetabolicoService: ProfiloMetabolicoService,
    private profiloService: ProfiloService
  ) {}

  ngOnInit(): void {
    this.caricaTutto();
  }

  get datiBiometriciMancanti(): boolean {
    return !this.profilo?.sesso || !this.profilo?.dataNascita;
  }

  salvaDatiBiometrici(): void {
    if (!this.formBiometrici.sesso || !this.formBiometrici.dataNascita) {
      return;
    }
    this.salvataggioBiometriciInCorso = true;
    this.erroreBiometrici = null;

    this.profiloService.aggiornaDatiBiometrici({
      sesso: this.formBiometrici.sesso,
      dataNascita: this.formBiometrici.dataNascita
    }).subscribe({
      next: () => {
        this.salvataggioBiometriciInCorso = false;
        this.caricaTutto();
      },
      error: () => {
        this.salvataggioBiometriciInCorso = false;
        this.erroreBiometrici = 'Impossibile salvare i dati biometrici. Controlla i dati e riprova.';
      }
    });
  }

  calcola(): void {
    if (!this.formMetabolico.livelloAttivita) {
      return;
    }
    this.calcoloInCorso = true;
    this.erroreCalcolo = null;

    this.profiloMetabolicoService.calcola({
      livelloAttivita: this.formMetabolico.livelloAttivita,
      formulaForzata: this.formMetabolico.formulaForzata
    }).subscribe({
      next: () => {
        this.calcoloInCorso = false;
        this.caricaTutto();
      },
      error: (err) => {
        this.calcoloInCorso = false;
        this.erroreCalcolo = err.status === 404
          ? 'Registra prima almeno una misurazione corporea.'
          : (err.error?.messaggio ?? 'Impossibile calcolare il profilo metabolico. Riprova.');
      }
    });
  }

  private caricaTutto(): void {
    this.caricamento = true;
    this.errore = false;

    forkJoin({
      profilo: this.profiloService.getProfilo(),
      storico: this.profiloMetabolicoService.getStorico()
    }).subscribe({
      next: (risultati) => {
        this.profilo = risultati.profilo;
        this.storico = risultati.storico;
        if (this.profilo.sesso) {
          this.formBiometrici.sesso = this.profilo.sesso;
        }
        if (this.profilo.dataNascita) {
          this.formBiometrici.dataNascita = this.profilo.dataNascita;
        }
        this.caricamento = false;
      },
      error: () => {
        this.errore = true;
        this.caricamento = false;
      }
    });

    this.profiloMetabolicoService.getAttuale().subscribe({
      next: (attuale) => this.attuale = attuale,
      error: () => this.attuale = null
    });
  }
}
