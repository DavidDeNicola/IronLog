import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { LoginComponent } from './pages/login/login.component';
import { authGuard } from './app.guard';
import { SchedeComponent } from './pages/schede/schede.component';
import { DettaglioSchedaComponent } from './pages/dettaglio-scheda/dettaglio-scheda.component';
import { EserciziComponent } from './pages/esercizi/esercizi.component';
import { AllenamentoComponent } from './pages/allenamento/allenamento.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'schede', component: SchedeComponent },
      { path: 'schede/:id', component: DettaglioSchedaComponent },
      { path: 'esercizi', component: EserciziComponent },
      { path: 'allenamento', component: AllenamentoComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: 'dashboard' }
];
