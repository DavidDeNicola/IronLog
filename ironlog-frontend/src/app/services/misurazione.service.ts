import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { MisurazioneCorporea, MisurazioneCreazione } from '../models/nutrizione.model';

@Injectable({ providedIn: 'root' })
export class MisurazioneService {

  private baseUrl = `${environment.apiUrl}/atleta/misurazioni`;

  constructor(private http: HttpClient) {}

  registra(misurazione: MisurazioneCreazione): Observable<MisurazioneCorporea> {
    return this.http.post<MisurazioneCorporea>(this.baseUrl, misurazione);
  }

  getMisurazioni(): Observable<MisurazioneCorporea[]> {
    return this.http.get<MisurazioneCorporea[]>(this.baseUrl);
  }

  getUltima(): Observable<MisurazioneCorporea> {
    return this.http.get<MisurazioneCorporea>(`${this.baseUrl}/ultima`);
  }
}
