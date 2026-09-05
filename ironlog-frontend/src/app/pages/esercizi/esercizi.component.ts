import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Subject, debounceTime, switchMap } from 'rxjs';

import { CatalogoService } from '../../services/catalogo.service';
import { GruppoMuscolare, Esercizio } from '../../models/catalogo.model';

@Component({
  selector: 'app-esercizi',
  imports: [FormsModule],
  templateUrl: './esercizi.component.html',
  styleUrl: './esercizi.component.scss'
})
export class EserciziComponent implements OnInit {

  gruppi: GruppoMuscolare[] = [];
  esercizi: Esercizio[] = [];
  caricamento = true;
  errore = false;

  testoRicerca = '';
  gruppoSelezionato: number | null = null;

  private ricerca$ = new Subject<void>();

  constructor(private catalogoService: CatalogoService) {}

  ngOnInit(): void {
    this.catalogoService.getGruppi().subscribe({
      next: (gruppi) => this.gruppi = gruppi,
      error: () => this.errore = true
    });

    this.ricerca$
      .pipe(
        debounceTime(300),
        switchMap(() => this.catalogoService.cerca(this.testoRicerca, this.gruppoSelezionato))
      )
      .subscribe({
        next: (esercizi) => {
          this.esercizi = esercizi;
          this.caricamento = false;
        },
        error: () => {
          this.errore = true;
          this.caricamento = false;
        }
      });

    this.ricerca$.next();
  }

  onRicercaCambiata(): void {
    this.caricamento = true;
    this.ricerca$.next();
  }

  selezionaGruppo(id: number | null): void {
    this.gruppoSelezionato = id;
    this.onRicercaCambiata();
  }
}
