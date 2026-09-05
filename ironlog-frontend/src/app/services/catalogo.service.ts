import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { GruppoMuscolare, Esercizio } from '../models/catalogo.model';

@Injectable({ providedIn: 'root' })
export class CatalogoService {

  private baseUrl = `${environment.apiUrl}/catalogo`;

  constructor(private http: HttpClient) {}

  getGruppi(): Observable<GruppoMuscolare[]> {
    return this.http.get<GruppoMuscolare[]>(`${this.baseUrl}/gruppi`);
  }

  getEsercizi(): Observable<Esercizio[]> {
    return this.http.get<Esercizio[]>(`${this.baseUrl}/esercizi`);
  }

  cerca(nome: string | null, gruppoId: number | null): Observable<Esercizio[]> {
    let params = new HttpParams();

    if (nome) {
      params = params.set('nome', nome);
    }
    if (gruppoId !== null) {
      params = params.set('gruppoId', gruppoId);
    }

    return this.http.get<Esercizio[]>(`${this.baseUrl}/esercizi/cerca`, { params });
  }
}
