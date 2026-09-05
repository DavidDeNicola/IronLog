export interface ProssimoAllenamento {
  giornoSchedaId: number;
  giornoNome: string;
  schedaNome: string;
  numeroEsercizi: number;
}

export interface SessioneRequest {
  giornoSchedaId: number | null;
  note: string | null;
}

export interface SerieEseguitaRequest {
  esercizioSchedaId: number | null;
  esercizioId: number;
  ripetizioni: number;
  peso: number;
}

export interface SerieEseguitaResponse {
  id: number;
  esercizioId: number;
  numeroSerie: number;
  esercizioNome: string;
  ripetizioni: number;
  peso: number;
  ripObiettivo: number | null;
  massimaleStimato: number;
}

export interface SessioneResponse {
  id: number;
  giornoSchedaId: number | null;
  schedaId: number | null;
  eseguitaIl: string;
  conclusaIl: string | null;
  giornoNome: string | null;
  schedaNome: string | null;
  note: string | null;
  serie: SerieEseguitaResponse[];
}

export interface RiepilogoSessione {
  sessioneId: number;
  eseguitaIl: string;
  conclusaIl: string;
  durataMinuti: number;
  numeroSerie: number;
  volumeTotale: number;
  serieCompletate: number;
}
