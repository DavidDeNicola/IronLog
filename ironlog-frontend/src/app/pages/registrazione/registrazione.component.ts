import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { AuthService } from '../../services/auth.service';
import { RegisterRequest, Ruolo } from '../../models/auth.model';
import { Cliente } from '../../models/coach.model';

@Component({
  selector: 'app-registrazione',
  imports: [FormsModule, RouterLink],
  templateUrl: './registrazione.component.html',
  styleUrl: './registrazione.component.scss'
})
export class RegistrazioneComponent implements OnInit {

  dati: RegisterRequest = {
    nome: '',
    cognome: '',
    email: '',
    password: '',
    ruolo: 'ATHLETE',
    coachId: null
  };

  confermaPassword = '';
  coach: Cliente[] = [];

  errore: string | null = null;
  caricamento = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authService.getCoachDisponibili().subscribe({
      next: (coach) => this.coach = coach,
      error: () => {}
    });
  }

  get atleta(): boolean {
    return this.dati.ruolo === 'ATHLETE';
  }

  selezionaRuolo(ruolo: Ruolo): void {
    this.dati.ruolo = ruolo;
    if (ruolo === 'COACH') {
      this.dati.coachId = null;
    }
  }

  onSubmit(): void {
    this.errore = null;

    if (this.dati.password.length < 8) {
      this.errore = 'La password deve avere almeno 8 caratteri.';
      return;
    }

    if (this.dati.password !== this.confermaPassword) {
      this.errore = 'La password e la conferma non coincidono.';
      return;
    }

    this.caricamento = true;

    this.authService.register(this.dati).subscribe({
      next: () => {
        // Registrazione riuscita: si effettua subito il login con le stesse credenziali.
        this.authService.login({ email: this.dati.email, password: this.dati.password })
          .subscribe({
            next: () => {
              this.caricamento = false;
              const destinazione = this.authService.getRuolo() === 'COACH'
                ? '/coach/clienti'
                : '/dashboard';
              this.router.navigate([destinazione]);
            },
            error: () => {
              this.caricamento = false;
              this.router.navigate(['/login']);
            }
          });
      },
      error: (err) => {
        this.caricamento = false;
        this.errore = err.status === 409
          ? 'Esiste già un account con questa email.'
          : 'Registrazione non riuscita. Controlla i dati e riprova.';
      }
    });
  }
}
