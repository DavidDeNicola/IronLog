import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { AssunzioneIntegratore, AssunzioneIntegratoreCreazione, Integratore, IntegratoreCreazione } from '../models/nutrizione.model';

@Injectable({ providedIn: 'root' })
export class IntegratoreService {

  private catalogoUrl = `${environment.apiUrl}/catalogo/integratori`;
  private assunzioniUrl = `${environment.apiUrl}/atleta/integratori/assunzioni`;

  constructor(private http: HttpClient) {}

  getIntegratori(): Observable<Integratore[]> {
    return this.http.get<Integratore[]>(this.catalogoUrl);
  }

  creaIntegratore(integratore: IntegratoreCreazione): Observable<Integratore> {
    return this.http.post<Integratore>(this.catalogoUrl, integratore);
  }

  registraAssunzione(assunzione: AssunzioneIntegratoreCreazione): Observable<AssunzioneIntegratore> {
    return this.http.post<AssunzioneIntegratore>(this.assunzioniUrl, assunzione);
  }

  getAssunzioni(data: string): Observable<AssunzioneIntegratore[]> {
    const params = new HttpParams().set('data', data);
    return this.http.get<AssunzioneIntegratore[]>(this.assunzioniUrl, { params });
  }
}
