import { Component, OnInit, AfterViewInit, OnDestroy, ViewChild, ElementRef, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { Chart, registerables } from 'chart.js';
import { Subscription } from 'rxjs';

import { MisurazioneService } from '../../services/misurazione.service';
import { ThemeService } from '../../services/theme.service';
import { MisurazioneCorporea, MisurazioneCreazione } from '../../models/nutrizione.model';

Chart.register(...registerables);

@Component({
  selector: 'app-misurazioni',
  imports: [FormsModule, DatePipe],
  templateUrl: './misurazioni.component.html',
  styleUrl: './misurazioni.component.scss'
})
export class MisurazioniComponent implements OnInit, AfterViewInit, OnDestroy {

  misurazioni: MisurazioneCorporea[] = [];
  caricamento = true;
  errore = false;

  form: { data: string; pesoKg: number | null; altezzaCm: number | null; percentualeMassaGrassa: number | null; note: string } = {
    data: new Date().toISOString().substring(0, 10),
    pesoKg: null,
    altezzaCm: null,
    percentualeMassaGrassa: null,
    note: ''
  };

  salvataggioInCorso = false;
  erroreSalvataggio: string | null = null;

  @ViewChild('canvasPeso') canvasPeso?: ElementRef<HTMLCanvasElement>;

  private graficoPeso?: Chart;
  private vistaPronta = false;
  private sottoscrizioneTema?: Subscription;

  constructor(
    private misurazioneService: MisurazioneService,
    private themeService: ThemeService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.caricaMisurazioni();

    this.sottoscrizioneTema = this.themeService.cambiamenti.subscribe(() => {
      this.graficoPeso?.destroy();
      this.graficoPeso = undefined;
      this.disegnaGrafico();
    });
  }

  ngAfterViewInit(): void {
    this.vistaPronta = true;
    this.disegnaGrafico();
  }

  ngOnDestroy(): void {
    this.sottoscrizioneTema?.unsubscribe();
    this.graficoPeso?.destroy();
  }

  get valido(): boolean {
    return !!this.form.data && this.form.pesoKg !== null && this.form.pesoKg > 0
      && this.form.altezzaCm !== null && this.form.altezzaCm > 0;
  }

  salva(): void {
    if (!this.valido) {
      return;
    }
    this.salvataggioInCorso = true;
    this.erroreSalvataggio = null;

    const richiesta: MisurazioneCreazione = {
      data: this.form.data,
      pesoKg: this.form.pesoKg as number,
      altezzaCm: this.form.altezzaCm as number,
      percentualeMassaGrassa: this.form.percentualeMassaGrassa,
      note: this.form.note?.trim() || null
    };

    this.misurazioneService.registra(richiesta).subscribe({
      next: () => {
        this.salvataggioInCorso = false;
        this.form = {
          data: new Date().toISOString().substring(0, 10),
          pesoKg: null,
          altezzaCm: null,
          percentualeMassaGrassa: null,
          note: ''
        };
        this.caricaMisurazioni();
      },
      error: () => {
        this.salvataggioInCorso = false;
        this.erroreSalvataggio = 'Impossibile salvare la misurazione. Controlla i dati e riprova.';
      }
    });
  }

  private caricaMisurazioni(): void {
    this.caricamento = true;
    this.errore = false;
    this.misurazioneService.getMisurazioni().subscribe({
      next: (misurazioni) => {
        this.misurazioni = misurazioni;
        this.caricamento = false;
        this.graficoPeso?.destroy();
        this.graficoPeso = undefined;
        this.cdr.detectChanges();
        this.disegnaGrafico();
      },
      error: () => {
        this.errore = true;
        this.caricamento = false;
      }
    });
  }

  private disegnaGrafico(): void {
    if (!this.vistaPronta || this.caricamento || !this.canvasPeso || this.graficoPeso) {
      return;
    }

    const colori = this.themeService.coloriGrafico();
    const ordinate = [...this.misurazioni].sort((a, b) => a.data.localeCompare(b.data));

    this.graficoPeso = new Chart(this.canvasPeso.nativeElement, {
      type: 'line',
      data: {
        labels: ordinate.map(m => this.formattaData(m.data)),
        datasets: [{
          data: ordinate.map(m => m.pesoKg),
          borderColor: colori.accento,
          backgroundColor: 'rgba(59, 130, 246, 0.1)',
          fill: true,
          tension: 0.3,
          pointRadius: 3
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { display: false } },
        scales: {
          x: { ticks: { color: colori.testo }, grid: { color: colori.griglia } },
          y: { ticks: { color: colori.testo }, grid: { color: colori.griglia } }
        }
      }
    });
  }

  private formattaData(iso: string): string {
    const data = new Date(iso);
    const mesi = ['gen', 'feb', 'mar', 'apr', 'mag', 'giu',
      'lug', 'ago', 'set', 'ott', 'nov', 'dic'];
    return `${data.getDate()} ${mesi[data.getMonth()]}`;
  }
}
