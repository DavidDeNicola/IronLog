import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { SchedaService } from '../../services/scheda.service';
import { CoachService } from '../../services/coach.service';
import { CatalogoService } from '../../services/catalogo.service';
import { Esercizio } from '../../models/catalogo.model';
import { SchedaCreazione, GiornoCreazione } from '../../models/scheda.model';

interface GruppoEsercizi {
  gruppo: string;
  esercizi: Esercizio[];
}

@Component({
  selector: 'app-crea-scheda',
  imports: [FormsModule],
  templateUrl: './crea-scheda.component.html',
  styleUrl: './crea-scheda.component.scss'
})
export class CreaSchedaComponent implements OnInit {

  scheda: SchedaCreazione = {
    nome: '',
    note: '',
    dataInizio: new Date().toISOString().substring(0, 10),
    giorni: []
  };

  gruppiEsercizi: GruppoEsercizi[] = [];
  caricamentoCatalogo = true;

  salvataggioInCorso = false;
  errore: string | null = null;

  atletaId: number | null = null;
  nomeCliente = '';

  constructor(
    private schedaService: SchedaService,
    private coachService: CoachService,
    private catalogoService: CatalogoService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.atletaId = Number(idParam);
      this.caricaNomeCliente(this.atletaId);
    }

    this.catalogoService.getEsercizi().subscribe({
      next: (esercizi) => {
        this.gruppiEsercizi = this.raggruppa(esercizi);
        this.caricamentoCatalogo = false;
      },
      error: () => {
        this.caricamentoCatalogo = false;
      }
    });

    this.aggiungiGiorno();
  }

  get modalitaCoach(): boolean {
    return this.atletaId !== null;
  }

  aggiungiGiorno(): void {
    this.scheda.giorni.push({
      nome: `Giorno ${this.scheda.giorni.length + 1}`,
      esercizi: []
    });
  }

  rimuoviGiorno(indice: number): void {
    this.scheda.giorni.splice(indice, 1);
  }

  aggiungiEsercizio(giorno: GiornoCreazione): void {
    giorno.esercizi.push({
      esercizioId: null,
      serie: 3,
      ripetizioni: 10,
      pesoAttuale: 0,
      recupero: 90
    });
  }

  rimuoviEsercizio(giorno: GiornoCreazione, indice: number): void {
    giorno.esercizi.splice(indice, 1);
  }

  get valida(): boolean {
    if (!this.scheda.nome.trim() || !this.scheda.dataInizio || this.scheda.giorni.length === 0) {
      return false;
    }
    return this.scheda.giorni.every(giorno =>
      giorno.nome.trim() !== '' &&
      giorno.esercizi.length > 0 &&
      giorno.esercizi.every(es =>
        es.esercizioId !== null &&
        es.serie !== null && es.serie > 0 &&
        es.ripetizioni !== null && es.ripetizioni > 0 &&
        es.pesoAttuale !== null && es.pesoAttuale >= 0 &&
        es.recupero !== null && es.recupero >= 0
      )
    );
  }

  salva(): void {
    if (!this.valida) {
      return;
    }
    this.salvataggioInCorso = true;
    this.errore = null;

    const richiesta: SchedaCreazione = {
      nome: this.scheda.nome.trim(),
      note: this.scheda.note?.trim() || null,
      dataInizio: this.scheda.dataInizio,
      giorni: this.scheda.giorni
    };

    const richiesta$ = this.atletaId !== null
      ? this.coachService.creaSchedaPerAtleta(this.atletaId, richiesta)
      : this.schedaService.creaScheda(richiesta);

    richiesta$.subscribe({
      next: (creata) => {
        if (this.atletaId !== null) {
          this.router.navigate(['/coach/clienti']);
        } else {
          this.router.navigate(['/schede', creata.id]);
        }
      },
      error: () => {
        this.salvataggioInCorso = false;
        this.errore = 'Impossibile salvare la scheda. Controlla i dati e riprova.';
      }
    });
  }

  annulla(): void {
    this.router.navigate([this.atletaId !== null ? '/coach/clienti' : '/schede']);
  }

  private caricaNomeCliente(atletaId: number): void {
    this.coachService.getAtleti().subscribe({
      next: (atleti) => {
        const cliente = atleti.find(a => a.id === atletaId);
        if (cliente) {
          this.nomeCliente = `${cliente.nome} ${cliente.cognome}`;
        }
      },
      error: () => {}
    });
  }

  private raggruppa(esercizi: Esercizio[]): GruppoEsercizi[] {
    const mappa = new Map<string, Esercizio[]>();
    for (const es of esercizi) {
      const lista = mappa.get(es.gruppoMuscolareNome) ?? [];
      lista.push(es);
      mappa.set(es.gruppoMuscolareNome, lista);
    }
    return Array.from(mappa, ([gruppo, esercizi]) => ({ gruppo, esercizi }));
  }
}
