import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { AuthService } from '../../services/auth.service';
import { LoginRequest } from '../../models/auth.model';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  credenziali: LoginRequest = {
    email: '',
    password: ''
  };

  errore: string | null = null;
  caricamento = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmit(): void {
    this.errore = null;
    this.caricamento = true;

    this.authService.login(this.credenziali).subscribe({
      next: () => {
        this.caricamento = false;
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.caricamento = false;
        this.errore = err.status === 401
          ? 'Email o password non corretti.'
          : 'Si è verificato un errore. Riprova più tardi.';
      }
    });
  }
}
