import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { RiepilogoDiario, VoceDiario, VoceDiarioCreazione } from '../models/nutrizione.model';

@Injectable({ providedIn: 'root' })
export class DiarioService {

  private baseUrl = `${environment.apiUrl}/atleta/diario`;

  constructor(private http: HttpClient) {}

  registraVoce(voce: VoceDiarioCreazione): Observable<VoceDiario> {
    return this.http.post<VoceDiario>(this.baseUrl, voce);
  }

  getVoci(data: string): Observable<VoceDiario[]> {
    const params = new HttpParams().set('data', data);
    return this.http.get<VoceDiario[]>(this.baseUrl, { params });
  }

  eliminaVoce(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  getRiepilogo(data: string): Observable<RiepilogoDiario> {
    const params = new HttpParams().set('data', data);
    return this.http.get<RiepilogoDiario>(`${this.baseUrl}/riepilogo`, { params });
  }
}
