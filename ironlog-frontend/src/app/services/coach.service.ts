import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { Cliente, StatisticaCliente } from '../models/coach.model';
import { Profilo, CambioPasswordRequest } from '../models/profilo.model';
import { SchedaCreazione, SchedaResponse, SchedaSintesi } from '../models/scheda.model';
import {
  AssunzioneIntegratore,
  ConsumoAcqua,
  Fase,
  FaseCreazione,
  FaseSintesi,
  MisurazioneCorporea,
  MisurazioneCreazione,
  PianoAlimentare,
  PianoAlimentareSintesi,
  ProfiloMetabolico,
  ProfiloMetabolicoCreazione,
  RiepilogoDiario,
  RisultatoImport,
  VoceDiario
} from '../models/nutrizione.model';

@Injectable({ providedIn: 'root' })
export class CoachService {

  private baseUrl = `${environment.apiUrl}/coach`;

  constructor(private http: HttpClient) {}

  getAtleti(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(`${this.baseUrl}/atleti`);
  }

  getProfilo(): Observable<Profilo> {
    return this.http.get<Profilo>(`${this.baseUrl}/profilo`);
  }

  getSchedeAtleta(atletaId: number): Observable<SchedaSintesi[]> {
    return this.http.get<SchedaSintesi[]>(`${this.baseUrl}/atleti/${atletaId}/schede`);
  }

  getStatisticheClienti(): Observable<StatisticaCliente[]> {
    return this.http.get<StatisticaCliente[]>(`${this.baseUrl}/statistiche/clienti`);
  }

  creaSchedaPerAtleta(atletaId: number, scheda: SchedaCreazione): Observable<SchedaResponse> {
    return this.http.post<SchedaResponse>(`${this.baseUrl}/atleti/${atletaId}/schede`, scheda);
  }

  cambiaPassword(richiesta: CambioPasswordRequest): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/profilo/password`, richiesta);
  }

  eliminaProfilo(): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/profilo`);
  }

  // Misurazioni corporee

  getMisurazioniAtleta(atletaId: number): Observable<MisurazioneCorporea[]> {
    return this.http.get<MisurazioneCorporea[]>(`${this.baseUrl}/atleti/${atletaId}/misurazioni`);
  }

  creaMisurazionePerAtleta(atletaId: number, misurazione: MisurazioneCreazione): Observable<MisurazioneCorporea> {
    return this.http.post<MisurazioneCorporea>(`${this.baseUrl}/atleti/${atletaId}/misurazioni`, misurazione);
  }

  // Profilo metabolico

  getProfiloMetabolicoAttualeAtleta(atletaId: number): Observable<ProfiloMetabolico> {
    return this.http.get<ProfiloMetabolico>(`${this.baseUrl}/atleti/${atletaId}/profilo-metabolico/attuale`);
  }

  calcolaProfiloMetabolicoPerAtleta(atletaId: number, richiesta: ProfiloMetabolicoCreazione): Observable<ProfiloMetabolico> {
    return this.http.post<ProfiloMetabolico>(`${this.baseUrl}/atleti/${atletaId}/profilo-metabolico`, richiesta);
  }

  // Fasi

  getFasiAtleta(atletaId: number): Observable<FaseSintesi[]> {
    return this.http.get<FaseSintesi[]>(`${this.baseUrl}/atleti/${atletaId}/fasi`);
  }

  getFaseAttivaAtleta(atletaId: number): Observable<Fase> {
    return this.http.get<Fase>(`${this.baseUrl}/atleti/${atletaId}/fasi/attiva`);
  }

  creaFasePerAtleta(atletaId: number, fase: FaseCreazione): Observable<Fase> {
    return this.http.post<Fase>(`${this.baseUrl}/atleti/${atletaId}/fasi`, fase);
  }

  chiudiFasePerAtleta(atletaId: number, id: number): Observable<Fase> {
    return this.http.put<Fase>(`${this.baseUrl}/atleti/${atletaId}/fasi/${id}/chiudi`, {});
  }

  // Diario alimentare (sola lettura)

  getDiarioAtleta(atletaId: number, data: string): Observable<VoceDiario[]> {
    const params = new HttpParams().set('data', data);
    return this.http.get<VoceDiario[]>(`${this.baseUrl}/atleti/${atletaId}/diario`, { params });
  }

  getRiepilogoDiarioAtleta(atletaId: number, data: string): Observable<RiepilogoDiario> {
    const params = new HttpParams().set('data', data);
    return this.http.get<RiepilogoDiario>(`${this.baseUrl}/atleti/${atletaId}/diario/riepilogo`, { params });
  }

  // Acqua (sola lettura)

  getAcquaAtleta(atletaId: number, data: string): Observable<ConsumoAcqua[]> {
    const params = new HttpParams().set('data', data);
    return this.http.get<ConsumoAcqua[]>(`${this.baseUrl}/atleti/${atletaId}/acqua`, { params });
  }

  // Integratori (sola lettura)

  getAssunzioniIntegratoriAtleta(atletaId: number, data: string): Observable<AssunzioneIntegratore[]> {
    const params = new HttpParams().set('data', data);
    return this.http.get<AssunzioneIntegratore[]>(`${this.baseUrl}/atleti/${atletaId}/integratori/assunzioni`, { params });
  }

  // Piani alimentari

  generaPianoAlimentarePerAtleta(atletaId: number, faseId: number): Observable<PianoAlimentare> {
    const params = new HttpParams().set('faseId', faseId);
    return this.http.post<PianoAlimentare>(`${this.baseUrl}/atleti/${atletaId}/piani-alimentari/genera`, {}, { params });
  }

  getPianiAlimentariAtleta(atletaId: number, faseId: number): Observable<PianoAlimentareSintesi[]> {
    const params = new HttpParams().set('faseId', faseId);
    return this.http.get<PianoAlimentareSintesi[]>(`${this.baseUrl}/atleti/${atletaId}/piani-alimentari`, { params });
  }

  // Import alimenti da CSV

  importaAlimenti(file: File): Observable<RisultatoImport> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<RisultatoImport>(`${this.baseUrl}/alimenti/import`, formData);
  }
}
