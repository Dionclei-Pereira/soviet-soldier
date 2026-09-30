import { Component, signal, inject } from '@angular/core';
import { NgForm } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';
import { take } from 'rxjs';

@Component({
  selector: 'app-auth-login',
  standalone: false,
  templateUrl: './auth-login.component.html',
  styleUrl: './auth-login.component.scss',
})
export class AuthLoginComponent {

  err = signal<boolean>(false);

  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  goToRegister(): void {
    this.router.navigate(['/auth/register']);
  }

  onSubmit(form: NgForm) {
    this.authService.login(form.value)
      .pipe(take(1))
      .subscribe({
        next: () => {
          this.router.navigate(['/home']);
        },
        error: () => {
          this.err.set(true);
        }
      });
  }
}
