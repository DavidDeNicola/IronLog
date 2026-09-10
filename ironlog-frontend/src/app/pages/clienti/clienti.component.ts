import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { CoachService } from '../../services/coach.service';
import { Cliente } from '../../models/coach.model';

@Component({
  selector: 'app-clienti',
  imports: [],
  templateUrl: './clienti.component.html',
  styleUrl: './clienti.component.scss'
})
export class ClientiComponent implements OnInit {

  clienti: Cliente[] = [];
  caricamento = true;
  errore = false;

  constructor(
    private coachService: CoachService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.coachService.getAtleti().subscribe({
      next: (clienti) => {
        this.clienti = clienti;
        this.caricamento = false;
      },
      error: () => {
        this.errore = true;
        this.caricamento = false;
      }
    });
  }

  iniziali(cliente: Cliente): string {
    return (cliente.nome.charAt(0) + cliente.cognome.charAt(0)).toUpperCase();
  }

  nuovaScheda(clienteId: number): void {
    this.router.navigate(['/coach/clienti', clienteId, 'scheda-nuova']);
  }
}
