import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Subject, debounceTime, switchMap } from 'rxjs';
import { environment } from '../../../environments/environment';
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
  soloPreferiti = false;

  private preferiti = new Set<number>();

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

    this.catalogoService.getPreferiti().subscribe({
      next: (preferiti) => this.preferiti = new Set(preferiti.map(e => e.id)),
      error: () => {}
    });

    this.ricerca$.next();
  }

  get eserciziVisibili(): Esercizio[] {
    return this.soloPreferiti
      ? this.esercizi.filter(e => this.preferiti.has(e.id))
      : this.esercizi;
  }

  isPreferito(es: Esercizio): boolean {
    return this.preferiti.has(es.id);
  }

  alternaPreferito(es: Esercizio): void {
    const eraPreferito = this.preferiti.has(es.id);

    // Aggiornamento ottimistico: l'icona reagisce subito, in caso di errore si ripristina.
    if (eraPreferito) {
      this.preferiti.delete(es.id);
    } else {
      this.preferiti.add(es.id);
    }

    const richiesta$ = eraPreferito
      ? this.catalogoService.rimuoviPreferito(es.id)
      : this.catalogoService.aggiungiPreferito(es.id);

    richiesta$.subscribe({
      error: () => {
        if (eraPreferito) {
          this.preferiti.add(es.id);
        } else {
          this.preferiti.delete(es.id);
        }
      }
    });
  }

  alternaFiltroPreferiti(): void {
    this.soloPreferiti = !this.soloPreferiti;
  }

  onRicercaCambiata(): void {
    this.caricamento = true;
    this.ricerca$.next();
  }

  selezionaGruppo(id: number | null): void {
    this.gruppoSelezionato = id;
    this.onRicercaCambiata();
  }

  urlImmagine(es: Esercizio): string | null {
    const percorso = es.immagineUrl ?? es.gruppoImmagineUrl;
    return percorso ? `${environment.apiUrl}${percorso}` : null;
  }

  onImmagineErrore(evento: Event): void {
    (evento.target as HTMLImageElement).style.display = 'none';
  }
}
