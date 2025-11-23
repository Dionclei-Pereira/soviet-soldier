import { Component, } from '@angular/core';
import { NgForm, NgModel } from '@angular/forms';
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

  constructor(private readonly authService: AuthService, private readonly router: Router) {
  }

  onSubmit(form: NgForm) {
    this.authService.register(form.value).pipe(
      take(1)
    ).subscribe({
      next: () => {
        this.router.navigate(['/auth/login']);
      },
      error: (err) => {
        console.log('Something went wrong');
      }
    });
  }

}
