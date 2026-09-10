import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';

import { CoachService } from '../../services/coach.service';
import { Cliente } from '../../models/coach.model';
import { SchedaSintesi } from '../../models/scheda.model';

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
