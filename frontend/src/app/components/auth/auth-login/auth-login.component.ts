import { Component } from '@angular/core';
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

  err: boolean = false;

  constructor(private readonly authService: AuthService, private readonly router: Router) { }

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
        error: (ex) => {
          console.log(ex)
          this.err = true;
        }
      });
  }
}
