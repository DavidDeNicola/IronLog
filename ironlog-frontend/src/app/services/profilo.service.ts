import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { Profilo, CambioPasswordRequest } from '../models/profilo.model';
import { DatiBiometriciRequest } from '../models/nutrizione.model';

@Injectable({ providedIn: 'root' })
export class ProfiloService {

  private baseUrl = `${environment.apiUrl}/atleta/profilo`;

  constructor(private http: HttpClient) {}

  getProfilo(): Observable<Profilo> {
    return this.http.get<Profilo>(this.baseUrl);
  }

  aggiornaDatiBiometrici(richiesta: DatiBiometriciRequest): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/dati-biometrici`, richiesta);
  }

  azzeraStatistiche(): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/atleta/sessioni`);
  }

  cambiaPassword(richiesta: CambioPasswordRequest): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/password`, richiesta);
  }

  eliminaProfilo(): Observable<void> {
    return this.http.delete<void>(this.baseUrl);
  }
}
