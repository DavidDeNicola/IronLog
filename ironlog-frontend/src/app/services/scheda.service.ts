import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { SchedaSintesi, SchedaResponse, SchedaCreazione } from '../models/scheda.model';

@Injectable({ providedIn: 'root' })
export class SchedaService {

  private baseUrl = `${environment.apiUrl}/atleta/schede`;

  constructor(private http: HttpClient) {}

  getSchede(): Observable<SchedaSintesi[]> {
    return this.http.get<SchedaSintesi[]>(this.baseUrl);
  }

  getScheda(id: number): Observable<SchedaResponse> {
    return this.http.get<SchedaResponse>(`${this.baseUrl}/${id}`);
  }

  creaScheda(scheda: SchedaCreazione): Observable<SchedaResponse> {
    return this.http.post<SchedaResponse>(this.baseUrl, scheda);
  }
}
