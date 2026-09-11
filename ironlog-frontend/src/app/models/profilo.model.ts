import { Ruolo } from './auth.model';
import { Sesso } from './nutrizione.model';

export interface Profilo {
  id: number;
  nome: string;
  cognome: string;
  email: string;
  ruolo: Ruolo;
  sesso: Sesso | null;
  dataNascita: string | null;
}

export interface CambioPasswordRequest {
  passwordAttuale: string;
  nuovaPassword: string;
}
