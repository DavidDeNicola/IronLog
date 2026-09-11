import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { forkJoin } from 'rxjs';

import { IntegratoreService } from '../../services/integratore.service';
import { AssunzioneIntegratore, Integratore, IntegratoreCreazione } from '../../models/nutrizione.model';

@Component({
  selector: 'app-integratori',
  imports: [FormsModule],
  templateUrl: './integratori.component.html',
  styleUrl: './integratori.component.scss'
})
export class IntegratoriComponent implements OnInit {

  data = new Date().toISOString().substring(0, 10);

  integratori: Integratore[] = [];
  assunzioni: AssunzioneIntegratore[] = [];

  caricamento = true;
  errore = false;

  formAssunzione: { integratoreId: number | null; dosaggioAssunto: number | null } = {
    integratoreId: null,
    dosaggioAssunto: null
  };
  registrazioneInCorso = false;
  erroreRegistrazione: string | null = null;

  mostraFormNuovo = false;
  formNuovo: IntegratoreCreazione = { nome: '', dosaggioDefault: 0, unita: '' };
  salvataggioCatalogoInCorso = false;
  erroreCatalogo: string | null = null;

  constructor(private integratoreService: IntegratoreService) {}

  ngOnInit(): void {
    this.caricaTutto();
  }

  cambiaData(): void {
    this.caricaAssunzioni();
  }

  selezionaIntegratore(): void {
    const scelto = this.integratori.find(i => i.id === this.formAssunzione.integratoreId);
    this.formAssunzione.dosaggioAssunto = scelto ? scelto.dosaggioDefault : null;
  }

  registraAssunzione(): void {
    if (!this.formAssunzione.integratoreId || !this.formAssunzione.dosaggioAssunto) {
      return;
    }
    this.registrazioneInCorso = true;
    this.erroreRegistrazione = null;

    this.integratoreService.registraAssunzione({
      integratoreId: this.formAssunzione.integratoreId,
      data: this.data,
      dosaggioAssunto: this.formAssunzione.dosaggioAssunto
    }).subscribe({
      next: (assunzione) => {
        this.assunzioni = [...this.assunzioni, assunzione];
        this.registrazioneInCorso = false;
      },
      error: () => {
        this.registrazioneInCorso = false;
        this.erroreRegistrazione = 'Impossibile registrare l\'assunzione. Riprova.';
      }
    });
  }

  apriFormNuovo(): void {
    this.mostraFormNuovo = true;
    this.erroreCatalogo = null;
  }

  annullaFormNuovo(): void {
    this.mostraFormNuovo = false;
    this.formNuovo = { nome: '', dosaggioDefault: 0, unita: '' };
  }

  salvaNuovoIntegratore(): void {
    if (!this.formNuovo.nome.trim() || !this.formNuovo.unita.trim() || this.formNuovo.dosaggioDefault <= 0) {
      return;
    }
    this.salvataggioCatalogoInCorso = true;
    this.erroreCatalogo = null;

    this.integratoreService.creaIntegratore(this.formNuovo).subscribe({
      next: (creato) => {
        this.integratori = [...this.integratori, creato];
        this.salvataggioCatalogoInCorso = false;
        this.annullaFormNuovo();
      },
      error: () => {
        this.salvataggioCatalogoInCorso = false;
        this.erroreCatalogo = 'Impossibile creare l\'integratore. Controlla i dati e riprova.';
      }
    });
  }

  private caricaTutto(): void {
    this.caricamento = true;
    this.errore = false;

    forkJoin({
      integratori: this.integratoreService.getIntegratori(),
      assunzioni: this.integratoreService.getAssunzioni(this.data)
    }).subscribe({
      next: (risultati) => {
        this.integratori = risultati.integratori;
        this.assunzioni = risultati.assunzioni;
        this.caricamento = false;
      },
      error: () => {
        this.errore = true;
        this.caricamento = false;
      }
    });
  }

  private caricaAssunzioni(): void {
    this.integratoreService.getAssunzioni(this.data).subscribe({
      next: (assunzioni) => this.assunzioni = assunzioni,
      error: () => {}
    });
  }
}
