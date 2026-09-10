import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

type Tema = 'scuro' | 'chiaro';

/** Colori derivati dalle variabili CSS, usati dai grafici Chart.js. */
export interface ColoriGrafico {
  testo: string;
  griglia: string;
  sfondoCard: string;
  accento: string;
}

@Injectable({ providedIn: 'root' })
export class ThemeService {

  private readonly CHIAVE = 'ironlog_tema';
  private readonly CLASSE_CHIARO = 'tema-chiaro';

  private tema: Tema;
  private readonly tema$: BehaviorSubject<Tema>;

  constructor() {
    const salvato = localStorage.getItem(this.CHIAVE) as Tema | null;
    this.tema = salvato === 'chiaro' ? 'chiaro' : 'scuro';
    this.tema$ = new BehaviorSubject<Tema>(this.tema);
    this.applica();
  }

  get temaChiaro(): boolean {
    return this.tema === 'chiaro';
  }

  /** Emette a ogni cambio di tema: i grafici si ridisegnano con i nuovi colori. */
  get cambiamenti(): Observable<Tema> {
    return this.tema$.asObservable();
  }

  alterna(): void {
    this.tema = this.tema === 'scuro' ? 'chiaro' : 'scuro';
    localStorage.setItem(this.CHIAVE, this.tema);
    this.applica();
    this.tema$.next(this.tema);
  }

  /**
   * Legge i colori dalle variabili CSS definite in styles.scss, cosi' i grafici
   * seguono il tema attivo invece di avere valori fissi.
   */
  coloriGrafico(): ColoriGrafico {
    const stile = getComputedStyle(document.documentElement);
    const leggi = (nome: string, fallback: string) =>
      stile.getPropertyValue(nome).trim() || fallback;

    return {
      testo: leggi('--testo-secondario', '#8b98a9'),
      griglia: leggi('--bordo-card', '#262f3d'),
      sfondoCard: leggi('--sfondo-card', '#1a212c'),
      accento: leggi('--accento', '#3b82f6')
    };
  }

  private applica(): void {
    const root = document.documentElement;
    if (this.tema === 'chiaro') {
      root.classList.add(this.CLASSE_CHIARO);
    } else {
      root.classList.remove(this.CLASSE_CHIARO);
    }
  }
}
