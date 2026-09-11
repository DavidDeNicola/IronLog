import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { ProfiloMetabolico, ProfiloMetabolicoCreazione } from '../models/nutrizione.model';

@Injectable({ providedIn: 'root' })
export class ProfiloMetabolicoService {

  private baseUrl = `${environment.apiUrl}/atleta/profilo-metabolico`;

  constructor(private http: HttpClient) {}

  calcola(richiesta: ProfiloMetabolicoCreazione): Observable<ProfiloMetabolico> {
    return this.http.post<ProfiloMetabolico>(this.baseUrl, richiesta);
  }

  getAttuale(): Observable<ProfiloMetabolico> {
    return this.http.get<ProfiloMetabolico>(`${this.baseUrl}/attuale`);
  }

  getStorico(): Observable<ProfiloMetabolico[]> {
    return this.http.get<ProfiloMetabolico[]>(this.baseUrl);
  }
}
