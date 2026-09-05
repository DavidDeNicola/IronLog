import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';

import { SchedaService } from '../../services/scheda.service';
import { SchedaResponse } from '../../models/scheda.model';

@Component({
  selector: 'app-dettaglio-scheda',
  imports: [DatePipe],
  templateUrl: './dettaglio-scheda.component.html',
  styleUrl: './dettaglio-scheda.component.scss'
})
export class DettaglioSchedaComponent implements OnInit {

  scheda: SchedaResponse | null = null;
  caricamento = true;
  errore = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private schedaService: SchedaService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (!idParam) {
      this.router.navigate(['/schede']);
      return;
    }

    const id = Number(idParam);

    this.schedaService.getScheda(id).subscribe({
      next: (scheda) => {
        this.scheda = scheda;
        this.caricamento = false;
      },
      error: () => {
        this.errore = true;
        this.caricamento = false;
      }
    });
  }

  tornaIndietro(): void {
    this.router.navigate(['/schede']);
  }
}
