import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { Fase, FaseCreazione, FaseSintesi } from '../models/nutrizione.model';

@Injectable({ providedIn: 'root' })
export class FaseService {

  private baseUrl = `${environment.apiUrl}/atleta/fasi`;

  constructor(private http: HttpClient) {}

  creaFase(fase: FaseCreazione): Observable<Fase> {
    return this.http.post<Fase>(this.baseUrl, fase);
  }

  getFasi(): Observable<FaseSintesi[]> {
    return this.http.get<FaseSintesi[]>(this.baseUrl);
  }

  getFaseAttiva(): Observable<Fase> {
    return this.http.get<Fase>(`${this.baseUrl}/attiva`);
  }

  chiudiFase(id: number): Observable<Fase> {
    return this.http.put<Fase>(`${this.baseUrl}/${id}/chiudi`, {});
  }
}
