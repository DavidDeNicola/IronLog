import { Component, OnInit, AfterViewInit, OnDestroy, ViewChild, ElementRef, ChangeDetectorRef } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { Router } from '@angular/router';
import { forkJoin, Subscription } from 'rxjs';

import { StatisticheService } from '../../services/statistiche.service';
import { ThemeService } from '../../services/theme.service';
import { SessioneService } from '../../services/sessione.service';
import { AuthService } from '../../services/auth.service';
import { Dashboard, VolumeGruppo, PuntoVolume } from '../../models/statistiche.model';
import { ProssimoAllenamento } from '../../models/sessione.model';
import { SchedaSintesi } from '../../models/scheda.model';
import { SchedaService } from '../../services/scheda.service';
import { ProfiloService } from '../../services/profilo.service';

Chart.register(...registerables);

const COLORI = [
  '#3b82f6', '#8b5cf6', '#ec4899', '#f59e0b',
  '#10b981', '#06b6d4', '#ef4444', '#84cc16', '#f97316'
];

@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit, AfterViewInit, OnDestroy {

  caricamento = true;
  errore = false;

  riepilogo: Dashboard | null = null;
  volumeGruppi: VolumeGruppo[] = [];
  andamento: PuntoVolume[] = [];
  prossimo: ProssimoAllenamento | null = null;
  schede: SchedaSintesi[] = [];

  @ViewChild('canvasAndamento') canvasAndamento?: ElementRef<HTMLCanvasElement>;
  @ViewChild('canvasDonut') canvasDonut?: ElementRef<HTMLCanvasElement>;

  private graficoAndamento?: Chart;
  private graficoDonut?: Chart;
  private vistaPronta = false;
  private sottoscrizioneTema?: Subscription;
  private nomeProfilo = '';

  constructor(
    private statisticheService: StatisticheService,
    private sessioneService: SessioneService,
    private schedaService: SchedaService,
    private profiloService: ProfiloService,
    private themeService: ThemeService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnDestroy(): void {
    this.sottoscrizioneTema?.unsubscribe();
    this.graficoAndamento?.destroy();
    this.graficoDonut?.destroy();
  }

  ngAfterViewInit(): void {
    this.vistaPronta = true;
    this.disegnaGrafici();
    }

  ngOnInit(): void {
    forkJoin({
      riepilogo: this.statisticheService.dashboard(),
      volume: this.statisticheService.volumePerGruppo('SETTIMANA'),
      andamento: this.statisticheService.andamentoVolume('SETTIMANA'),
      schede: this.schedaService.getSchede()
    }).subscribe({
      next: (risultati) => {
        this.riepilogo = risultati.riepilogo;
        this.volumeGruppi = risultati.volume;
        this.andamento = risultati.andamento;
        this.schede = risultati.schede;
        this.caricamento = false;
        this.cdr.detectChanges();
        this.disegnaGrafici();
      },
      error: () => {
        this.errore = true;
        this.caricamento = false;
      }
    });

    this.sessioneService.getProssimo().subscribe({
      next: (prossimo) => this.prossimo = prossimo,
      error: () => {}
    });

    this.profiloService.getProfilo().subscribe({
      next: (profilo) => this.nomeProfilo = profilo.nome,
      error: () => {}
    });

    // Al cambio di tema i grafici vanno ricostruiti con i nuovi colori.
    this.sottoscrizioneTema = this.themeService.cambiamenti.subscribe(() => {
      this.graficoAndamento?.destroy();
      this.graficoDonut?.destroy();
      this.graficoAndamento = undefined;
      this.graficoDonut = undefined;
      this.disegnaGrafici();
    });
  }

  get nomeUtente(): string {
    if (this.nomeProfilo) {
      return this.nomeProfilo;
    }
    return this.authService.getUtente()?.sub.split('@')[0] ?? '';
  }

  avviaProssimo(): void {
    if (!this.prossimo) {
      return;
    }
    const schedaAttiva = this.schede.find(s => s.attiva);
    if (schedaAttiva) {
      this.router.navigate(['/allenamento/sessione'], {
        queryParams: { giorno: this.prossimo.giornoSchedaId, scheda: schedaAttiva.id }
      });
    }
  }

  private disegnaGrafici(): void {
    if (!this.vistaPronta || this.caricamento) {
      return;
    }

    const colori = this.themeService.coloriGrafico();

    if (this.canvasAndamento && !this.graficoAndamento) {
      this.graficoAndamento = new Chart(this.canvasAndamento.nativeElement, {
        type: 'line',
        data: {
          labels: this.andamento.map(p => this.formattaData(p.data)),
          datasets: [{
            data: this.andamento.map(p => p.volume),
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

    if (this.canvasDonut && !this.graficoDonut) {
      this.graficoDonut = new Chart(this.canvasDonut.nativeElement, {
        type: 'doughnut',
        data: {
          labels: this.volumeGruppi.map(v => v.gruppoMuscolareNome),
          datasets: [{
            data: this.volumeGruppi.map(v => v.volumeTotale),
            backgroundColor: COLORI,
            borderColor: colori.sfondoCard,
            borderWidth: 2
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          cutout: '62%',
          plugins: {
            legend: {
              position: 'bottom',
              labels: { color: colori.testo, padding: 12, boxWidth: 12, font: { size: 11 } }
            }
          }
        }
      });
    }
  }

  private formattaData(iso: string): string {
    const data = new Date(iso);
    const mesi = ['gen', 'feb', 'mar', 'apr', 'mag', 'giu',
      'lug', 'ago', 'set', 'ott', 'nov', 'dic'];
    return `${data.getDate()} ${mesi[data.getMonth()]}`;
  }
}
