import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { catchError, map, of, take } from 'rxjs';

export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);

  return authService.isLoggedIn().pipe(
    catchError(() => {
      authService.logout();
      return of(false);
    }),
    map(isValid => {
      if (!isValid) {
        authService.logout();
        return false;
      }

      return true;
    })
  );
};
