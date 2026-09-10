import { Injectable } from '@angular/core';

type Tema = 'scuro' | 'chiaro';

@Injectable({ providedIn: 'root' })
export class ThemeService {

  private readonly CHIAVE = 'ironlog_tema';
  private readonly CLASSE_CHIARO = 'tema-chiaro';

  private tema: Tema;

  constructor() {
    const salvato = localStorage.getItem(this.CHIAVE) as Tema | null;
    this.tema = salvato === 'chiaro' ? 'chiaro' : 'scuro';
    this.applica();
  }

  get temaChiaro(): boolean {
    return this.tema === 'chiaro';
  }

  alterna(): void {
    this.tema = this.tema === 'scuro' ? 'chiaro' : 'scuro';
    localStorage.setItem(this.CHIAVE, this.tema);
    this.applica();
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
