import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { VolumeGruppo, PuntoVolume, Dashboard, RiepilogoStatistiche, PeriodoStatistica } from '../models/statistiche.model';

@Injectable({ providedIn: 'root' })
export class StatisticheService {

  private baseUrl = `${environment.apiUrl}/atleta/statistiche`;

  constructor(private http: HttpClient) {}

  volumePerGruppo(periodo: PeriodoStatistica): Observable<VolumeGruppo[]> {
    const params = new HttpParams().set('periodo', periodo);
    return this.http.get<VolumeGruppo[]>(`${this.baseUrl}/volume`, { params });
  }

  andamentoVolume(periodo: PeriodoStatistica): Observable<PuntoVolume[]> {
    const params = new HttpParams().set('periodo', periodo);
    return this.http.get<PuntoVolume[]>(`${this.baseUrl}/andamento`, { params });
  }

  dashboard(): Observable<Dashboard> {
    return this.http.get<Dashboard>(`${this.baseUrl}/dashboard`);
  }

  riepilogo(periodo: PeriodoStatistica): Observable<RiepilogoStatistiche> {
    const params = new HttpParams().set('periodo', periodo);
    return this.http.get<RiepilogoStatistiche>(`${this.baseUrl}/riepilogo`, { params });
  }
}
