export interface SchedaSintesi {
  id: number;
  nome: string;
  dataInizio: string;
  attiva: boolean;
  numeroGiorni: number;
}

export interface EsercizioScheda {
  id: number;
  esercizioNome: string;
  ordine: number;
  serie: number;
  ripetizioni: number;
  pesoAttuale: number;
  recupero: number;
}

export interface GiornoScheda {
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
