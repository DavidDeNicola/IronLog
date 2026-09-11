export type Sesso = 'MASCHIO' | 'FEMMINA';
export type LivelloAttivita = 'SEDENTARIO' | 'LEGGERMENTE_ATTIVO' | 'MODERATAMENTE_ATTIVO' | 'MOLTO_ATTIVO' | 'ESTREMAMENTE_ATTIVO';
export type FormulaMetabolica = 'MIFFLIN_ST_JEOR' | 'KATCH_MCARDLE';
export type TipoFase = 'BULK' | 'CUT' | 'MAINTENANCE' | 'REVERSE_DIET' | 'MINI_CUT' | 'DIET_BREAK';
export type TipoPasto = 'COLAZIONE' | 'PRANZO' | 'CENA' | 'SPUNTINO';
export type FonteAlimento = 'IMPORT_ESTERNO' | 'PERSONALIZZATO';

export interface DatiBiometriciRequest {
  sesso: Sesso;
  dataNascita: string;
}

export interface MisurazioneCorporea {
  id: number;
  data: string;
  pesoKg: number;
  altezzaCm: number;
  percentualeMassaGrassa: number | null;
  note: string | null;
}

export interface MisurazioneCreazione {
  data: string;
  pesoKg: number;
  altezzaCm: number;
  percentualeMassaGrassa?: number | null;
  note?: string | null;
}

export interface ProfiloMetabolico {
  id: number;
  calcolatoIl: string;
  bmr: number;
  tdee: number;
  livelloAttivita: LivelloAttivita;
  formula: FormulaMetabolica;
}

export interface ProfiloMetabolicoCreazione {
  livelloAttivita: LivelloAttivita;
  formulaForzata?: FormulaMetabolica | null;
}

export interface FaseSintesi {
  id: number;
  tipo: TipoFase;
  dataInizio: string;
  dataFine: string | null;
  attiva: boolean;
}

export interface Fase {
  id: number;
  tipo: TipoFase;
  dataInizio: string;
  dataFine: string | null;
  baseTargetCalorico: number;
  targetCaloricoAttuale: number;
  targetProteineG: number;
  targetGrassiG: number;
  targetCarboidratiG: number;
  durataSuggeritaMesi: number | null;
  giorniTrascorsi: number;
}

export interface FaseCreazione {
  tipo: TipoFase;
  dataInizio: string;
  targetCaloricoOverride?: number | null;
  targetProteineGOverride?: number | null;
  targetGrassiGOverride?: number | null;
  targetCarboidratiGOverride?: number | null;
}

export interface MicronutrienteVoce {
  nomeNutriente: string;
  quantita: number;
  unita: string;
}

export interface Alimento {
  id: number;
  nome: string;
  fonte: FonteAlimento;
  calorie100g: number;
  proteineG: number;
  grassiG: number;
  carboidratiG: number;
  fibreG: number | null;
  zuccheriG: number | null;
  sodioMg: number | null;
  micronutrienti: MicronutrienteVoce[];
}

export interface AlimentoCreazione {
  nome: string;
  calorie100g: number;
  proteineG: number;
  grassiG: number;
  carboidratiG: number;
  fibreG?: number | null;
  zuccheriG?: number | null;
  sodioMg?: number | null;
  micronutrienti?: MicronutrienteVoce[];
}

export interface Integratore {
  id: number;
  nome: string;
  dosaggioDefault: number;
  unita: string;
}

export interface IntegratoreCreazione {
  nome: string;
  dosaggioDefault: number;
  unita: string;
}

export interface VoceDiario {
  id: number;
  alimentoId: number;
  alimentoNome: string;
  data: string;
  tipoPasto: TipoPasto;
  quantitaGrammi: number;
  calorie: number;
  proteineG: number;
  grassiG: number;
  carboidratiG: number;
}

export interface VoceDiarioCreazione {
  alimentoId: number;
  data: string;
  tipoPasto: TipoPasto;
  quantitaGrammi: number;
}

export interface RiepilogoDiario {
  data: string;
  calorieTotali: number;
  proteineTotali: number;
  grassiTotali: number;
  carboidratiTotali: number;
  targetCalorico: number | null;
  differenzaCalorica: number | null;
}

export interface ConsumoAcqua {
  id: number;
  data: string;
  mlConsumati: number;
  mlObiettivo: number | null;
}

export interface ConsumoAcquaCreazione {
  data: string;
  mlConsumati: number;
  mlObiettivo?: number | null;
}

export interface AssunzioneIntegratore {
  id: number;
  integratoreId: number;
  integratoreNome: string;
  data: string;
  dosaggioAssunto: number;
}

export interface AssunzioneIntegratoreCreazione {
  integratoreId: number;
  data: string;
  dosaggioAssunto: number;
}

export interface VocePianoAlimentare {
  alimentoId: number;
  alimentoNome: string;
  tipoPasto: TipoPasto;
  quantitaGrammi: number;
  calorie: number;
  proteineG: number;
  grassiG: number;
  carboidratiG: number;
}

export interface PianoAlimentareSintesi {
  id: number;
  dataGenerazione: string;
  vincoliSoddisfatti: boolean;
}

export interface PianoAlimentare {
  id: number;
  faseId: number;
  dataGenerazione: string;
  vincoliSoddisfatti: boolean;
  voci: VocePianoAlimentare[];
  calorieTotali: number;
  proteineTotali: number;
  grassiTotali: number;
  carboidratiTotali: number;
}

export interface GruppoPasto {
  tipoPasto: TipoPasto;
  etichetta: string;
  icona: string;
  voci: VocePianoAlimentare[];
  calorieTotali: number;
}

export interface RisultatoImport {
  righeLette: number;
  alimentiCreati: number;
  alimentiAggiornati: number;
  righeScartate: number;
  errori: string[];
}
