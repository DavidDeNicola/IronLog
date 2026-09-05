export interface GruppoMuscolare {
  id: number;
  nome: string;
}

export interface Esercizio {
  id: number;
  nome: string;
  descrizione: string;
  gruppoMuscolareNome: string;
}
