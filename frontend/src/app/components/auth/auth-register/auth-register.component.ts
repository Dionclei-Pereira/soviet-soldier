import { Component, inject } from '@angular/core';
import { NgForm } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { take } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-auth-register',
  standalone: false,
  templateUrl: './auth-register.component.html',
  styleUrl: './auth-register.component.scss',
})
export class AuthRegisterComponent {

  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  onSubmit(form: NgForm) {
    this.authService.register(form.value).pipe(
      take(1)
    ).subscribe({
      next: () => {
        this.router.navigate(['/auth/login']);
      },
      error: () => {
        console.log('Something went wrong');
      }
    });
  }

}
