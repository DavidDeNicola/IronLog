export interface SchedaSintesi {
  id: number;
  nome: string;
  dataInizio: string;
  attiva: boolean;
  numeroGiorni: number;
}

export interface EsercizioScheda {
  id: number;
  esercizioId: number;
  esercizioNome: string;
  ordine: number;
  serie: number;
  ripetizioni: number;
  pesoAttuale: number;
  recupero: number;
}

export interface GiornoScheda {
  id: number;
  nome: string;
  ordine: number;
  esercizi: EsercizioScheda[];
}

export interface SchedaResponse {
  id: number;
  nome: string;
  dataInizio: string;
  attiva: boolean;
  numeroGiorni: number;
  note: string | null;
  atletaNome: string;
  autoreNome: string;
  giorni: GiornoScheda[];
}

export interface EsercizioCreazione {
  esercizioId: number | null;
  serie: number | null;
  ripetizioni: number | null;
  pesoAttuale: number | null;
  recupero: number | null;
}

export interface GiornoCreazione {
  nome: string;
  esercizi: EsercizioCreazione[];
}

export interface SchedaCreazione {
  nome: string;
  note: string | null;
  dataInizio: string;
  giorni: GiornoCreazione[];
}
