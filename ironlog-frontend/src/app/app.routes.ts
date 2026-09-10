import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { LoginComponent } from './pages/login/login.component';
import { authGuard } from './app.guard';
import { SchedeComponent } from './pages/schede/schede.component';
import { DettaglioSchedaComponent } from './pages/dettaglio-scheda/dettaglio-scheda.component';
import { EserciziComponent } from './pages/esercizi/esercizi.component';
import { AllenamentoComponent } from './pages/allenamento/allenamento.component';
import { SessioneComponent } from './pages/sessione/sessione.component';
import { StoricoComponent } from './pages/storico/storico.component';
import { StatisticheComponent } from './pages/statistiche/statistiche.component';
import { ProfiloComponent } from './pages/profilo/profilo.component';
import {CreaSchedaComponent} from './pages/crea-scheda/crea-scheda.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
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
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: 'dashboard' }
];
