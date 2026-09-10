export type Ruolo = 'ATHLETE' | 'COACH';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  nome: string;
  cognome: string;
  email: string;
  password: string;
  ruolo: Ruolo;
  /** Solo per gli atleti: coach da cui farsi seguire. Facoltativo. */
  coachId: number | null;
}

export interface UtenteResponse {
  id: number;
  nome: string;
  cognome: string;
  email: string;
  ruolo: Ruolo;
}

export interface PayloadToken {
  sub: string;
  ruolo: Ruolo;
  iss: string;
  exp: number;
  iat: number;
}
