import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { SessioneService } from '../../services/sessione.service';
import { SchedaService } from '../../services/scheda.service';
import { SessioneResponse } from '../../models/sessione.model';
import { GiornoScheda, EsercizioScheda } from '../../models/scheda.model';
import { SerieEseguitaRequest } from '../../models/sessione.model';
import { RiepilogoSessione } from '../../models/sessione.model';


@Component({
  selector: 'app-sessione',
  imports: [],
  templateUrl: './sessione.component.html',
  styleUrl: './sessione.component.scss'
})
export class SessioneComponent implements OnInit, OnDestroy {

  caricamento = true;
  errore = false;

  sessione: SessioneResponse | null = null;
  giorno: GiornoScheda | null = null;
  inputSerie: { [esercizioSchedaId: number]: { peso: string; ripetizioni: string } } = {};
  salvataggioInCorso = false;

  recuperoAttivo = false;
  secondiRimanenti = 0;
  recuperoTotale = 0;
  private intervallo: ReturnType<typeof setInterval> | null = null;

  riepilogo: RiepilogoSessione | null = null;
  conclusioneInCorso = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private sessioneService: SessioneService,
    private schedaService: SchedaService
  ) {}

  ngOnDestroy(): void {
    this.fermaRecupero();
  }

  ngOnInit(): void {
    const ripresa = this.route.snapshot.queryParamMap.get('ripresa');

    if (ripresa === 'true') {
      this.riprendiSessioneAperta();
      return;
    }

    const giornoId = Number(this.route.snapshot.queryParamMap.get('giorno'));
    const schedaId = Number(this.route.snapshot.queryParamMap.get('scheda'));

    if (!giornoId || !schedaId) {
      this.router.navigate(['/allenamento']);
      return;
    }

    this.schedaService.getScheda(schedaId).subscribe({
      next: (scheda) => {
        const giorno = scheda.giorni.find(g => g.id === giornoId);
        if (!giorno) {
          this.errore = true;
          this.caricamento = false;
          return;
        }
        this.giorno = giorno;
        this.apriSessione(giornoId);
      },
      error: () => {
        this.errore = true;
        this.caricamento = false;
      }
    });
  }

  /*
   * I due campi sono di tipo testo e il valore digitato resta una stringa fino
   * al momento in cui la serie viene registrata.
   *
   * Con <input type="number"> e la lettura di valueAsNumber, digitare il
   * separatore decimale produceva un valore intermedio non valido ("20.",
   * "20,"): valueAsNumber restituiva NaN, il binding [value] riscriveva NaN
   * nell'input e il browser svuotava la casella, rendendo impossibile inserire
   * i mezzi chili. Tenendo il testo cosi' com'e' il problema non si presenta, e
   * la conversione avviene una sola volta, alla conferma.
   */

  pesoInput(es: EsercizioScheda): string {
    return this.inputSerie[es.id]?.peso ?? '';
  }

  impostaPeso(es: EsercizioScheda, valore: string): void {
    this.assicuraInput(es.id);
    this.inputSerie[es.id].peso = valore;
  }

  ripInput(es: EsercizioScheda): string {
    return this.inputSerie[es.id]?.ripetizioni ?? '';
  }

  impostaRip(es: EsercizioScheda, valore: string): void {
    this.assicuraInput(es.id);
    this.inputSerie[es.id].ripetizioni = valore;
  }

  /** Peso valido: numero non negativo, con la virgola accettata al posto del punto. */
  pesoNumerico(es: EsercizioScheda): number | null {
    return this.aNumero(this.pesoInput(es));
  }

  /** Ripetizioni valide: numero intero maggiore di zero. */
  ripNumeriche(es: EsercizioScheda): number | null {
    const valore = this.aNumero(this.ripInput(es));
    if (valore === null || valore < 1) {
      return null;
    }
    return Math.trunc(valore);
  }

  private aNumero(testo: string): number | null {
    const pulito = testo.trim().replace(',', '.');
    if (pulito === '') {
      return null;
    }
    const numero = Number(pulito);
    return Number.isFinite(numero) && numero >= 0 ? numero : null;
  }

  private assicuraInput(id: number): void {
    if (!this.inputSerie[id]) {
      this.inputSerie[id] = { peso: '', ripetizioni: '' };
    }
  }

  serieDi(esercizioIdCatalogo: number): number {
    if (!this.sessione) {
      return 0;
    }
    return this.sessione.serie.filter(s => s.esercizioId === esercizioIdCatalogo).length;
  }

  registraSerie(es: EsercizioScheda): void {
    const peso = this.pesoNumerico(es);
    const ripetizioni = this.ripNumeriche(es);

    if (peso === null || ripetizioni === null || !this.sessione) {
      return;
    }

    const richiesta: SerieEseguitaRequest = {
      esercizioSchedaId: es.id,
      esercizioId: es.esercizioId,
      peso: peso,
      ripetizioni: ripetizioni
    };

    this.salvataggioInCorso = true;

    this.sessioneService.registraSerie(this.sessione.id, richiesta).subscribe({
      next: (serie) => {
        this.sessione!.serie.push(serie);
        this.inputSerie[es.id] = { peso: this.pesoInput(es), ripetizioni: '' };
        this.salvataggioInCorso = false;
        this.avviaRecupero(es.recupero);
      },
      error: () => {
        this.salvataggioInCorso = false;
      }
    });
  }

  avviaRecupero(secondi: number): void {
    this.fermaRecupero();
    this.recuperoTotale = secondi;
    this.secondiRimanenti = secondi;
    this.recuperoAttivo = true;
    this.intervallo = setInterval(() => {
      this.secondiRimanenti--;
      if (this.secondiRimanenti <= 0) {
        this.fermaRecupero();
      }
    }, 1000);
  }

  fermaRecupero(): void {
    if (this.intervallo !== null) {
      clearInterval(this.intervallo);
      this.intervallo = null;
    }
    this.recuperoAttivo = false;
  }

  formattaTempo(secondi: number): string {
    const min = Math.floor(secondi / 60);
    const sec = secondi % 60;
    return `${min}:${sec.toString().padStart(2, '0')}`;
  }

  concludi(): void {
    if (!this.sessione) {
      return;
    }
    this.conclusioneInCorso = true;
    this.fermaRecupero();
    this.sessioneService.concludi(this.sessione.id).subscribe({
      next: (riepilogo) => {
        this.riepilogo = riepilogo;
        this.conclusioneInCorso = false;
      },
      error: () => {
        this.conclusioneInCorso = false;
      }
    });
  }

  tornaAllenamento(): void {
    this.router.navigate(['/allenamento']);
  }

  private apriSessione(giornoSchedaId: number): void {
    this.sessioneService.apri({ giornoSchedaId, note: null }).subscribe({
      next: (sessione) => {
        this.sessione = sessione;
        this.caricamento = false;
      },
      error: (err) => {
        if (err.status === 409) {
          this.recuperaAperta();
        } else {
          this.errore = true;
          this.caricamento = false;
        }
      }
    });
  }

  private recuperaAperta(): void {
    this.sessioneService.getAperta().subscribe({
      next: (sessione) => {
        this.sessione = sessione;
        this.caricamento = false;
      },
      error: () => {
        this.errore = true;
        this.caricamento = false;
      }
    });
  }

  private riprendiSessioneAperta(): void {
    this.sessioneService.getAperta().subscribe({
      next: (sessione) => {
        if (!sessione || sessione.schedaId === null || sessione.giornoSchedaId === null) {
          this.router.navigate(['/allenamento']);
          return;
        }
        this.sessione = sessione;
        this.caricaGiorno(sessione.schedaId, sessione.giornoSchedaId);
      },
      error: () => {
        this.errore = true;
        this.caricamento = false;
      }
    });
  }

  private caricaGiorno(schedaId: number, giornoSchedaId: number): void {
    this.schedaService.getScheda(schedaId).subscribe({
      next: (scheda) => {
        const giorno = scheda.giorni.find(g => g.id === giornoSchedaId);
        if (!giorno) {
          this.errore = true;
        } else {
          this.giorno = giorno;
        }
        this.caricamento = false;
      },
      error: () => {
        this.errore = true;
        this.caricamento = false;
      }
    });
  }

  get gradoProgresso(): number {
    if (this.recuperoTotale === 0) {
      return 0;
    }
    const frazioneRimanente = this.secondiRimanenti / this.recuperoTotale;
    return frazioneRimanente * 360;
  }
}
