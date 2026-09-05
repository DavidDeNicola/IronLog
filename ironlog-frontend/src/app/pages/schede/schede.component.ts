import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';

import { SchedaService } from '../../services/scheda.service';
import { SchedaSintesi } from '../../models/scheda.model';

@Component({
  selector: 'app-schede',
  imports: [DatePipe],
  templateUrl: './schede.component.html',
  styleUrl: './schede.component.scss'
})
export class SchedeComponent implements OnInit {

  schede: SchedaSintesi[] = [];
  caricamento = true;
  errore = false;

  constructor(
    private schedaService: SchedaService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.schedaService.getSchede().subscribe({
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

  apriScheda(id: number): void {
    this.router.navigate(['/schede', id]);
  }
}
