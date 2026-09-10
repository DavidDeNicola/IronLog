import { Ruolo } from './auth.model';

export interface Profilo {
  id: number;
  nome: string;
  cognome: string;
  email: string;
  ruolo: Ruolo;
}
