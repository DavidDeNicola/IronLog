import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { Profilo } from '../models/profilo.model';

@Injectable({ providedIn: 'root' })
export class ProfiloService {

  private baseUrl = `${environment.apiUrl}/atleta/profilo`;

  constructor(private http: HttpClient) {}

  getProfilo(): Observable<Profilo> {
    return this.http.get<Profilo>(this.baseUrl);
  }

  azzeraStatistiche(): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/atleta/sessioni`);
  }
}
