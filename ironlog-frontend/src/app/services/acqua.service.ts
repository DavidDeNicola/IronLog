import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { ConsumoAcqua, ConsumoAcquaCreazione } from '../models/nutrizione.model';

@Injectable({ providedIn: 'root' })
export class AcquaService {

  private baseUrl = `${environment.apiUrl}/atleta/acqua`;

  constructor(private http: HttpClient) {}

  registra(consumo: ConsumoAcquaCreazione): Observable<ConsumoAcqua> {
    return this.http.post<ConsumoAcqua>(this.baseUrl, consumo);
  }

  getConsumi(data: string): Observable<ConsumoAcqua[]> {
    const params = new HttpParams().set('data', data);
    return this.http.get<ConsumoAcqua[]>(this.baseUrl, { params });
  }
}
