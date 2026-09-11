import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';

import { CoachService } from '../../services/coach.service';
import { Cliente } from '../../models/coach.model';
import { SchedaSintesi } from '../../models/scheda.model';
import { AssunzioneIntegratore, ConsumoAcqua, Fase, MisurazioneCorporea, RiepilogoDiario } from '../../models/nutrizione.model';

@Component({
  selector: 'app-cliente-dettaglio',
  imports: [DatePipe],
  templateUrl: './cliente-dettaglio.component.html',
  styleUrl: './cliente-dettaglio.component.scss'
})
export class ClienteDettaglioComponent implements OnInit {

  clienteId!: number;
  cliente: Cliente | null = null;
  schede: SchedaSintesi[] = [];
  caricamento = true;
  errore = false;

  faseAttiva: Fase | null = null;
  ultimeMisurazioni: MisurazioneCorporea[] = [];
  riepilogoDiario: RiepilogoDiario | null = null;
  acquaOggi: ConsumoAcqua[] = [];
  assunzioniOggi: AssunzioneIntegratore[] = [];
  caricamentoNutrizione = true;

  constructor(
    private coachService: CoachService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.clienteId = Number(this.route.snapshot.paramMap.get('id'));

    this.coachService.getAtleti().subscribe({
      next: (atleti) => {
        this.cliente = atleti.find(a => a.id === this.clienteId) ?? null;
      },
      error: () => {}
    });

    this.coachService.getSchedeAtleta(this.clienteId).subscribe({
      next: (schede) => {
        this.schede = schede;
        this.caricamento = false;
      },
      error: () => {
        this.errore = true;
        this.caricamento = false;
      }
    });

    this.caricaNutrizione();
  }

  get totaleAcquaOggi(): number {
    return this.acquaOggi.reduce((somma, c) => somma + c.mlConsumati, 0);
  }

  get badgeAdattato(): boolean {
    return !!this.faseAttiva && this.faseAttiva.targetCaloricoAttuale !== this.faseAttiva.baseTargetCalorico;
  }

  private caricaNutrizione(): void {
    const oggi = new Date().toISOString().substring(0, 10);
    this.caricamentoNutrizione = true;

    this.coachService.getFaseAttivaAtleta(this.clienteId).subscribe({
      next: (fase) => this.faseAttiva = fase,
      error: () => this.faseAttiva = null
    });

    this.coachService.getMisurazioniAtleta(this.clienteId).subscribe({
      next: (misurazioni) => this.ultimeMisurazioni = misurazioni.slice(0, 3),
      error: () => this.ultimeMisurazioni = []
    });

    this.coachService.getRiepilogoDiarioAtleta(this.clienteId, oggi).subscribe({
      next: (riepilogo) => this.riepilogoDiario = riepilogo,
      error: () => this.riepilogoDiario = null
    });

    this.coachService.getAcquaAtleta(this.clienteId, oggi).subscribe({
      next: (acqua) => this.acquaOggi = acqua,
      error: () => this.acquaOggi = []
    });

    this.coachService.getAssunzioniIntegratoriAtleta(this.clienteId, oggi).subscribe({
      next: (assunzioni) => {
        this.assunzioniOggi = assunzioni;
        this.caricamentoNutrizione = false;
      },
      error: () => {
        this.assunzioniOggi = [];
        this.caricamentoNutrizione = false;
      }
    });
  }

  get iniziali(): string {
    if (!this.cliente) {
      return '';
    }
    return (this.cliente.nome.charAt(0) + this.cliente.cognome.charAt(0)).toUpperCase();
  }

  nuovaScheda(): void {
    this.router.navigate(['/coach/clienti', this.clienteId, 'scheda-nuova']);
  }

  indietro(): void {
    this.router.navigate(['/coach/clienti']);
  }
}
