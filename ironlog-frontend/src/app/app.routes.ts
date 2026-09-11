import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { LoginComponent } from './pages/login/login.component';
import { RegistrazioneComponent } from './pages/registrazione/registrazione.component';
import {authGuard, coachGuard} from './app.guard';
import { SchedeComponent } from './pages/schede/schede.component';
import { DettaglioSchedaComponent } from './pages/dettaglio-scheda/dettaglio-scheda.component';
import { EserciziComponent } from './pages/esercizi/esercizi.component';
import { AllenamentoComponent } from './pages/allenamento/allenamento.component';
import { SessioneComponent } from './pages/sessione/sessione.component';
import { StoricoComponent } from './pages/storico/storico.component';
import { StatisticheComponent } from './pages/statistiche/statistiche.component';
import { ProfiloComponent } from './pages/profilo/profilo.component';
import {CreaSchedaComponent} from './pages/crea-scheda/crea-scheda.component';
import {ClientiComponent} from './pages/clienti/clienti.component';
import {ClienteDettaglioComponent} from './pages/cliente-dettaglio/cliente-dettaglio.component';
import {ProfiloCoachComponent} from './pages/profilo-coach/profilo-coach.component';
import {StatisticheClientiComponent} from './pages/statistiche-clienti/statistiche-clienti.component';
import {MisurazioniComponent} from './pages/misurazioni/misurazioni.component';
import {ProfiloMetabolicoComponent} from './pages/profilo-metabolico/profilo-metabolico.component';
import {FasiComponent} from './pages/fasi/fasi.component';
import {DiarioAlimentareComponent} from './pages/diario-alimentare/diario-alimentare.component';
import {AcquaComponent} from './pages/acqua/acqua.component';
import {IntegratoriComponent} from './pages/integratori/integratori.component';
import {PianoAlimentareComponent} from './pages/piano-alimentare/piano-alimentare.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'registrazione', component: RegistrazioneComponent },
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'schede', component: SchedeComponent },
      { path: 'schede/nuova', component: CreaSchedaComponent },
      { path: 'schede/:id', component: DettaglioSchedaComponent },
      { path: 'esercizi', component: EserciziComponent },
      { path: 'allenamento', component: AllenamentoComponent },
      { path: 'allenamento/sessione', component: SessioneComponent },
      { path: 'storico', component: StoricoComponent },
      { path: 'statistiche', component: StatisticheComponent },
      { path: 'profilo', component: ProfiloComponent },
      { path: 'misurazioni', component: MisurazioniComponent },
      { path: 'profilo-metabolico', component: ProfiloMetabolicoComponent },
      { path: 'fasi', component: FasiComponent },
      { path: 'diario-alimentare', component: DiarioAlimentareComponent },
      { path: 'acqua', component: AcquaComponent },
      { path: 'integratori', component: IntegratoriComponent },
      { path: 'piano-alimentare', component: PianoAlimentareComponent },
      { path: 'coach/clienti', component: ClientiComponent, canActivate: [coachGuard] },
      { path: 'coach/clienti/:id', component: ClienteDettaglioComponent, canActivate: [coachGuard] },
      { path: 'coach/clienti/:id/scheda-nuova', component: CreaSchedaComponent, canActivate: [coachGuard] },
      { path: 'coach/statistiche', component: StatisticheClientiComponent, canActivate: [coachGuard] },
      { path: 'coach/profilo', component: ProfiloCoachComponent, canActivate: [coachGuard] },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: 'dashboard' }
];
