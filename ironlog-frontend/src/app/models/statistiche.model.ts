export type PeriodoStatistica = 'SETTIMANA' | 'MESE';

export interface VolumeGruppo {
  gruppoMuscolareNome: string;
  volumeTotale: number;
  numeroSerie: number;
}

export interface PuntoVolume {
  data: string;
  volume: number;
}

export interface Dashboard {
  allenamentiSettimana: number;
  volumeSettimana: number;
  serieSettimana: number;
  streak: number;
  variazioneVolumePercentuale: number | null;
}

export interface RiepilogoStatistiche {
  volumeTotale: number;
  serieTotali: number;
  numeroAllenamenti: number;
  mediaPerAllenamento: number;
}
