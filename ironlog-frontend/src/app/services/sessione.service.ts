import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import {
  ProssimoAllenamento,
  SessioneRequest,
  SessioneResponse,
  SerieEseguitaRequest,
  SerieEseguitaResponse,
  RiepilogoSessione
} from '../models/sessione.model';

@Injectable({ providedIn: 'root' })
export class SessioneService {

  private baseUrl = `${environment.apiUrl}/atleta/sessioni`;
  private schedeUrl = `${environment.apiUrl}/atleta/schede`;

  constructor(private http: HttpClient) {}

  getProssimo(): Observable<ProssimoAllenamento> {
    return this.http.get<ProssimoAllenamento>(`${this.schedeUrl}/prossimo`);
  }

  getAperta(): Observable<SessioneResponse> {
    return this.http.get<SessioneResponse>(`${this.baseUrl}/aperta`);
  }

  apri(richiesta: SessioneRequest): Observable<SessioneResponse> {
    return this.http.post<SessioneResponse>(this.baseUrl, richiesta);
  }

  registraSerie(sessioneId: number, serie: SerieEseguitaRequest): Observable<SerieEseguitaResponse> {
    return this.http.post<SerieEseguitaResponse>(`${this.baseUrl}/${sessioneId}/serie`, serie);
  }

  concludi(sessioneId: number): Observable<RiepilogoSessione> {
    return this.http.patch<RiepilogoSessione>(`${this.baseUrl}/${sessioneId}/conclusione`, {});
  }
}
