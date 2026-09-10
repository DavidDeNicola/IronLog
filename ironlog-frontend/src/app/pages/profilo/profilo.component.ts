import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { ProfiloService } from '../../services/profilo.service';
import { Profilo } from '../../models/profilo.model';

@Component({
  selector: 'app-profilo',
  imports: [FormsModule],
  templateUrl: './profilo.component.html',
  styleUrl: './profilo.component.scss'
})
export class ProfiloComponent implements OnInit {

  profilo?: Profilo;
  caricamento = true;
  errore = false;

  // Azzera statistiche
  confermaAzzera = false;
  azzeramentoInCorso = false;
  azzeraEsito: 'successo' | 'errore' | null = null;

  // Cambio password
  password = {
    attuale: '',
    nuova: '',
    conferma: ''
  };
  cambioInCorso = false;
  cambioErrore: string | null = null;
  cambioSuccesso = false;

  constructor(private profiloService: ProfiloService) {}

  ngOnInit(): void {
    this.profiloService.getProfilo().subscribe({
      next: (profilo) => {
        this.profilo = profilo;
        this.caricamento = false;
      },
      error: () => {
        this.errore = true;
        this.caricamento = false;
      }
    });
  }

  get iniziali(): string {
    if (!this.profilo) {
      return '';
    }
    return (this.profilo.nome.charAt(0) + this.profilo.cognome.charAt(0)).toUpperCase();
  }

  get ruoloEtichetta(): string {
    if (!this.profilo) {
      return '';
    }
    return this.profilo.ruolo === 'COACH' ? 'Coach' : 'Atleta';
  }

  chiediConfermaAzzera(): void {
    this.confermaAzzera = true;
    this.azzeraEsito = null;
  }

  annullaAzzera(): void {
    this.confermaAzzera = false;
  }

  azzeraStatistiche(): void {
    this.azzeramentoInCorso = true;
    this.profiloService.azzeraStatistiche().subscribe({
      next: () => {
        this.azzeramentoInCorso = false;
        this.confermaAzzera = false;
        this.azzeraEsito = 'successo';
      },
      error: () => {
        this.azzeramentoInCorso = false;
        this.confermaAzzera = false;
        this.azzeraEsito = 'errore';
      }
    });
  }

  cambiaPassword(): void {
    this.cambioErrore = null;
    this.cambioSuccesso = false;

    if (this.password.nuova !== this.password.conferma) {
      this.cambioErrore = 'La nuova password e la conferma non coincidono.';
      return;
    }

    this.cambioInCorso = true;
    this.profiloService.cambiaPassword({
      passwordAttuale: this.password.attuale,
      nuovaPassword: this.password.nuova
    }).subscribe({
      next: () => {
        this.cambioInCorso = false;
        this.cambioSuccesso = true;
        this.password = { attuale: '', nuova: '', conferma: '' };
      },
      error: (err) => {
        this.cambioInCorso = false;
        this.cambioErrore = err.status === 400
          ? (err.error?.messaggio ?? 'La password attuale non è corretta.')
          : 'Impossibile cambiare la password. Riprova.';
      }
    });
  }
}
