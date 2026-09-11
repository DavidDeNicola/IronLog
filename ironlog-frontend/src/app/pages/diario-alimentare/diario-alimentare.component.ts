import { Component, OnInit, AfterViewInit, OnDestroy, ViewChild, ElementRef, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Chart, registerables } from 'chart.js';
import { Subject, Subscription, debounceTime, switchMap, forkJoin } from 'rxjs';

import { DiarioService } from '../../services/diario.service';
import { AlimentoService } from '../../services/alimento.service';
import { ThemeService } from '../../services/theme.service';
import { Alimento, AlimentoCreazione, RiepilogoDiario, TipoPasto, VoceDiario } from '../../models/nutrizione.model';

Chart.register(...registerables);

const COLORI_MACRO = ['#3b82f6', '#f59e0b', '#10b981'];

interface OpzionePasto {
  valore: TipoPasto;
  etichetta: string;
}

@Component({
  selector: 'app-diario-alimentare',
  imports: [FormsModule],
  templateUrl: './diario-alimentare.component.html',
  styleUrl: './diario-alimentare.component.scss'
})
export class DiarioAlimentareComponent implements OnInit, AfterViewInit, OnDestroy {

  readonly pasti: OpzionePasto[] = [
    { valore: 'COLAZIONE', etichetta: 'Colazione' },
    { valore: 'PRANZO', etichetta: 'Pranzo' },
    { valore: 'CENA', etichetta: 'Cena' },
    { valore: 'SPUNTINO', etichetta: 'Spuntino' }
  ];

  data = new Date().toISOString().substring(0, 10);

  voci: VoceDiario[] = [];
  riepilogo: RiepilogoDiario | null = null;

  caricamento = true;
  errore = false;

  ricercaAlimento = '';
  risultatiRicerca: Alimento[] = [];
  alimentoSelezionato: Alimento | null = null;

  formVoce: { tipoPasto: TipoPasto; quantitaGrammi: number | null } = {
    tipoPasto: 'COLAZIONE',
    quantitaGrammi: null
  };
  aggiuntaInCorso = false;
  erroreAggiunta: string | null = null;

  mostraFormNuovoAlimento = false;
  formNuovoAlimento: AlimentoCreazione = {
    nome: '', calorie100g: 0, proteineG: 0, grassiG: 0, carboidratiG: 0
  };
  salvataggioAlimentoInCorso = false;
  erroreNuovoAlimento: string | null = null;

  eliminazioneInCorsoId: number | null = null;

  @ViewChild('canvasMacro') canvasMacro?: ElementRef<HTMLCanvasElement>;

  private graficoMacro?: Chart;
  private vistaPronta = false;
  private sottoscrizioneTema?: Subscription;
  private ricerca$ = new Subject<void>();

  constructor(
    private diarioService: DiarioService,
    private alimentoService: AlimentoService,
    private themeService: ThemeService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.caricaGiorno();

    this.ricerca$
      .pipe(
        debounceTime(300),
        switchMap(() => this.alimentoService.cerca(this.ricercaAlimento))
      )
      .subscribe({
        next: (risultati) => this.risultatiRicerca = risultati,
        error: () => this.risultatiRicerca = []
      });

    this.sottoscrizioneTema = this.themeService.cambiamenti.subscribe(() => {
      this.graficoMacro?.destroy();
      this.graficoMacro = undefined;
      this.disegnaGrafico();
    });
  }

  ngAfterViewInit(): void {
    this.vistaPronta = true;
    this.disegnaGrafico();
  }

  ngOnDestroy(): void {
    this.sottoscrizioneTema?.unsubscribe();
    this.graficoMacro?.destroy();
  }

  cambiaData(): void {
    this.caricaGiorno();
  }

  vociPerPasto(pasto: TipoPasto): VoceDiario[] {
    return this.voci.filter(v => v.tipoPasto === pasto);
  }

  onRicercaCambiata(): void {
    if (!this.ricercaAlimento.trim()) {
      this.risultatiRicerca = [];
      return;
    }
    this.ricerca$.next();
  }

  selezionaAlimento(alimento: Alimento): void {
    this.alimentoSelezionato = alimento;
    this.risultatiRicerca = [];
    this.ricercaAlimento = '';
  }

  deselezionaAlimento(): void {
    this.alimentoSelezionato = null;
    this.formVoce.quantitaGrammi = null;
  }

  aggiungiVoce(): void {
    if (!this.alimentoSelezionato || !this.formVoce.quantitaGrammi || this.formVoce.quantitaGrammi <= 0) {
      return;
    }
    this.aggiuntaInCorso = true;
    this.erroreAggiunta = null;

    this.diarioService.registraVoce({
      alimentoId: this.alimentoSelezionato.id,
      data: this.data,
      tipoPasto: this.formVoce.tipoPasto,
      quantitaGrammi: this.formVoce.quantitaGrammi
    }).subscribe({
      next: () => {
        this.aggiuntaInCorso = false;
        this.deselezionaAlimento();
        this.caricaGiorno();
      },
      error: () => {
        this.aggiuntaInCorso = false;
        this.erroreAggiunta = 'Impossibile aggiungere l\'alimento al diario. Riprova.';
      }
    });
  }

  eliminaVoce(id: number): void {
    this.eliminazioneInCorsoId = id;
    this.diarioService.eliminaVoce(id).subscribe({
      next: () => {
        this.eliminazioneInCorsoId = null;
        this.caricaGiorno();
      },
      error: () => {
        this.eliminazioneInCorsoId = null;
      }
    });
  }

  apriFormNuovoAlimento(): void {
    this.mostraFormNuovoAlimento = true;
    this.erroreNuovoAlimento = null;
  }

  annullaFormNuovoAlimento(): void {
    this.mostraFormNuovoAlimento = false;
    this.formNuovoAlimento = { nome: '', calorie100g: 0, proteineG: 0, grassiG: 0, carboidratiG: 0 };
  }

  salvaNuovoAlimento(): void {
    if (!this.formNuovoAlimento.nome.trim()) {
      return;
    }
    this.salvataggioAlimentoInCorso = true;
    this.erroreNuovoAlimento = null;

    this.alimentoService.creaAlimento(this.formNuovoAlimento).subscribe({
      next: (creato) => {
        this.salvataggioAlimentoInCorso = false;
        this.annullaFormNuovoAlimento();
        this.selezionaAlimento(creato);
      },
      error: () => {
        this.salvataggioAlimentoInCorso = false;
        this.erroreNuovoAlimento = 'Impossibile creare l\'alimento. Controlla i dati e riprova.';
      }
    });
  }

  private caricaGiorno(): void {
    this.caricamento = true;
    this.errore = false;

    forkJoin({
      voci: this.diarioService.getVoci(this.data),
      riepilogo: this.diarioService.getRiepilogo(this.data)
    }).subscribe({
      next: (risultati) => {
        this.voci = risultati.voci;
        this.riepilogo = risultati.riepilogo;
        this.caricamento = false;
        this.graficoMacro?.destroy();
        this.graficoMacro = undefined;
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
    if (!this.vistaPronta || this.caricamento || !this.canvasMacro || this.graficoMacro || !this.riepilogo) {
      return;
    }

    const colori = this.themeService.coloriGrafico();

    this.graficoMacro = new Chart(this.canvasMacro.nativeElement, {
      type: 'doughnut',
      data: {
        labels: ['Proteine', 'Grassi', 'Carboidrati'],
        datasets: [{
          data: [this.riepilogo.proteineTotali, this.riepilogo.grassiTotali, this.riepilogo.carboidratiTotali],
          backgroundColor: COLORI_MACRO,
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
