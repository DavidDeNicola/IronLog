import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { Alimento, AlimentoCreazione } from '../models/nutrizione.model';

@Injectable({ providedIn: 'root' })
export class AlimentoService {

  private baseUrl = `${environment.apiUrl}/catalogo/alimenti`;

  constructor(private http: HttpClient) {}

  getAlimenti(): Observable<Alimento[]> {
    return this.http.get<Alimento[]>(this.baseUrl);
  }

  cerca(nome: string): Observable<Alimento[]> {
    const params = new HttpParams().set('nome', nome);
    return this.http.get<Alimento[]>(`${this.baseUrl}/cerca`, { params });
  }

  getAlimento(id: number): Observable<Alimento> {
    return this.http.get<Alimento>(`${this.baseUrl}/${id}`);
  }

  creaAlimento(alimento: AlimentoCreazione): Observable<Alimento> {
    return this.http.post<Alimento>(this.baseUrl, alimento);
  }
}
