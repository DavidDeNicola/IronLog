export interface Cliente {
  id: number;
  nome: string;
  cognome: string;
  email: string;
}

export type Progressione = 'IN_PROGRESSO' | 'STABILE' | 'IN_CALO' | 'DATI_INSUFFICIENTI';

export interface StatisticaCliente {
  atletaId: number;
  nome: string;
  cognome: string;
  numeroSchede: number;
  allenamentiUltimi30Giorni: number;
  progressione: Progressione;
}
