import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { PianoAlimentare, PianoAlimentareSintesi } from '../models/nutrizione.model';

@Injectable({ providedIn: 'root' })
export class PianoAlimentareService {

  private baseUrl = `${environment.apiUrl}/atleta/piani-alimentari`;

  constructor(private http: HttpClient) {}

  genera(faseId: number): Observable<PianoAlimentare> {
    const params = new HttpParams().set('faseId', faseId);
    return this.http.post<PianoAlimentare>(`${this.baseUrl}/genera`, {}, { params });
  }

  getPiani(faseId: number): Observable<PianoAlimentareSintesi[]> {
    const params = new HttpParams().set('faseId', faseId);
    return this.http.get<PianoAlimentareSintesi[]>(this.baseUrl, { params });
  }

  getPiano(id: number): Observable<PianoAlimentare> {
    return this.http.get<PianoAlimentare>(`${this.baseUrl}/${id}`);
  }
}
