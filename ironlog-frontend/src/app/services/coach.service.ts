import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { Cliente } from '../models/coach.model';
import { Profilo } from '../models/profilo.model';
import { SchedaCreazione, SchedaResponse } from '../models/scheda.model';

@Injectable({ providedIn: 'root' })
export class CoachService {

  private baseUrl = `${environment.apiUrl}/coach`;

  constructor(private http: HttpClient) {}

  getAtleti(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(`${this.baseUrl}/atleti`);
  }

  getProfilo(): Observable<Profilo> {
    return this.http.get<Profilo>(`${this.baseUrl}/profilo`);
  }

  creaSchedaPerAtleta(atletaId: number, scheda: SchedaCreazione): Observable<SchedaResponse> {
    return this.http.post<SchedaResponse>(`${this.baseUrl}/atleti/${atletaId}/schede`, scheda);
  }
}
