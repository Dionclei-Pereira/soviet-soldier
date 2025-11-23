import { Directive, forwardRef } from '@angular/core';
import { AbstractControl, AsyncValidator, NG_ASYNC_VALIDATORS, ValidationErrors } from '@angular/forms';
import { map, Observable } from "rxjs";
import { AuthService } from '../services/auth.service';

@Directive({
  selector: '[appUsernameValidatorDirective]',
  standalone: false,
  providers: [
    {
      provide: NG_ASYNC_VALIDATORS,
      useExisting: forwardRef(() => UsernameValidatorDirective),
      multi: true
    }
  ]
})
export class UsernameValidatorDirective implements AsyncValidator {

  constructor(private readonly authService: AuthService) {
  }

  validate(control: AbstractControl): Promise<ValidationErrors | null> | Observable<ValidationErrors | null> {
    return this.authService.isUsernameAvailable(control.value)
      .pipe(
        map(response => {
          if (response) return null

          else return { invalidUsername: true };
        })
      );
  }

}
