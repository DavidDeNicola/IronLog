import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, tap, map } from 'rxjs';
import { jwtDecode } from 'jwt-decode';

import { environment } from '../../environments/environment';
import { LoginRequest, RegisterRequest, UtenteResponse, PayloadToken, Ruolo } from '../models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly CHIAVE_TOKEN = 'ironlog_token';
  private baseUrl = `${environment.apiUrl}/auth`;

  private token: string | null = null;
  private payload: PayloadToken | null = null;

  constructor(private http: HttpClient) {
    this.caricaTokenSalvato();
  }

  login(credenziali: LoginRequest): Observable<void> {
    return this.http
      .post<void>(`${this.baseUrl}/login`, credenziali, { observe: 'response' })
      .pipe(
        tap((risposta: HttpResponse<void>) => {
          const header = risposta.headers.get('Authorization');
          if (header) {
            this.salvaToken(header.replace('Bearer ', ''));
          }
        }),
        map(() => undefined)
      );
  }

  register(dati: RegisterRequest): Observable<UtenteResponse> {
    return this.http.post<UtenteResponse>(`${this.baseUrl}/register`, dati);
  }

  logout(): void {
    this.token = null;
    this.payload = null;
    localStorage.removeItem(this.CHIAVE_TOKEN);
  }

  getToken(): string | null {
    return this.token;
  }

  getUtente(): PayloadToken | null {
    return this.payload;
  }

  getRuolo(): Ruolo | null {
    return this.payload ? this.payload.ruolo : null;
  }

  isAutenticato(): boolean {
    return this.token !== null;
  }

  private salvaToken(token: string): void {
    this.token = token;
    this.payload = jwtDecode<PayloadToken>(token);
    localStorage.setItem(this.CHIAVE_TOKEN, token);
  }

  private caricaTokenSalvato(): void {
    const token = localStorage.getItem(this.CHIAVE_TOKEN);
    if (!token) {
      return;
    }
    try {
      const payload = jwtDecode<PayloadToken>(token);
      if (payload.exp * 1000 <= Date.now()) {
        localStorage.removeItem(this.CHIAVE_TOKEN);
        return;
      }
      this.token = token;
      this.payload = payload;
    } catch {
      localStorage.removeItem(this.CHIAVE_TOKEN);
    }
  }
}
