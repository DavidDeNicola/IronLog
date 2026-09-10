import { Component, OnInit, AfterViewInit, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { Chart, registerables } from 'chart.js';

import { Subscription } from 'rxjs';

import { StatisticheService } from '../../services/statistiche.service';
import { ThemeService } from '../../services/theme.service';
import { VolumeGruppo, PuntoVolume, RiepilogoStatistiche, PeriodoStatistica } from '../../models/statistiche.model';

Chart.register(...registerables);

const COLORI = [
  '#3b82f6', '#8b5cf6', '#ec4899', '#f59e0b',
  '#10b981', '#06b6d4', '#ef4444', '#84cc16', '#f97316'
];

@Component({
  selector: 'app-statistiche',
  imports: [],
  templateUrl: './statistiche.component.html',
  styleUrl: './statistiche.component.scss'
})
export class StatisticheComponent implements OnInit, AfterViewInit, OnDestroy {

  @ViewChild('canvasAndamento') canvasAndamento?: ElementRef<HTMLCanvasElement>;

  periodo: PeriodoStatistica = 'SETTIMANA';
  private datiCaricati = 0;
  caricamento = true;
  errore = false;

  volumeGruppi: VolumeGruppo[] = [];
  andamento: PuntoVolume[] = [];
  riepilogo: RiepilogoStatistiche | null = null;

  private graficoAndamento?: Chart;
  private sottoscrizioneTema?: Subscription;


  constructor(
    private statisticheService: StatisticheService,
    private themeService: ThemeService
  ) {}

  ngOnInit(): void {
    this.caricaDati();

    // Al cambio di tema il grafico va ricostruito con i nuovi colori.
    this.sottoscrizioneTema = this.themeService.cambiamenti.subscribe(() => {
      this.graficoAndamento?.destroy();
      this.graficoAndamento = undefined;
      this.creaGrafici();
      this.aggiornaGrafici();
    });
  }

  ngAfterViewInit(): void {
    this.creaGrafici();
    this.aggiornaGrafici();
    setTimeout(() => {
      this.graficoAndamento?.resize();
    }, 100);
  }

  ngOnDestroy(): void {
    this.sottoscrizioneTema?.unsubscribe();
    this.graficoAndamento?.destroy();
  }

  cambiaPeriodo(periodo: PeriodoStatistica): void {
    if (this.periodo === periodo) {
      return;
    }
    this.periodo = periodo;
    this.caricaDati();
  }

  private caricaDati(): void {
    this.caricamento = true;
    this.errore = false;
    this.datiCaricati = 0;

    this.statisticheService.andamentoVolume(this.periodo).subscribe({
      next: (dati) => {
        this.andamento = dati;
        this.controllaCompletamento();
      },
      error: () => {
        this.errore = true;
        this.caricamento = false;
      }
    });

    this.statisticheService.volumePerGruppo(this.periodo).subscribe({
      next: (dati) => {
        this.volumeGruppi = dati;
        this.controllaCompletamento();
      },
      error: () => {
        this.errore = true;
        this.caricamento = false;
      }
    });

    this.statisticheService.riepilogo(this.periodo).subscribe({
      next: (dati) => {
        this.riepilogo = dati;
        this.controllaCompletamento();
      },
      error: () => {
        this.errore = true;
        this.caricamento = false;
      }
    });
  }

  private controllaCompletamento(): void {
    this.datiCaricati++;
    if (this.datiCaricati >= 3) {
      this.caricamento = false;
      this.aggiornaGrafici();
    }
  }

  private creaGrafici(): void {
    const colori = this.themeService.coloriGrafico();

    if (this.canvasAndamento) {
      this.graficoAndamento = new Chart(this.canvasAndamento.nativeElement, {
        type: 'line',
        data: { labels: [], datasets: [{
            data: [],
            borderColor: colori.accento,
            backgroundColor: 'rgba(59, 130, 246, 0.1)',
            fill: true,
            tension: 0.3,
            pointRadius: 3
          }] },
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
  }

  private aggiornaGrafici(): void {
    if (this.graficoAndamento) {
      this.graficoAndamento.data.labels = this.andamento.map(p => this.formattaData(p.data));
      this.graficoAndamento.data.datasets[0].data = this.andamento.map(p => p.volume);
      this.graficoAndamento.update();
    }
  }

  get barreVolume(): { nome: string; percentuale: number; colore: string }[] {
    const totale = this.volumeGruppi.reduce((somma, v) => somma + v.volumeTotale, 0);
    if (totale === 0) {
      return [];
    }
    return this.volumeGruppi.map((v, i) => ({
      nome: v.gruppoMuscolareNome,
      percentuale: Math.round((v.volumeTotale / totale) * 100),
      colore: COLORI[i % COLORI.length]
    }));
  }

  private formattaData(iso: string): string {
    const data = new Date(iso);
    const giorno = data.getDate();
    const mesi = ['gen', 'feb', 'mar', 'apr', 'mag', 'giu',
      'lug', 'ago', 'set', 'ott', 'nov', 'dic'];
    return `${giorno} ${mesi[data.getMonth()]}`;
  }
}
