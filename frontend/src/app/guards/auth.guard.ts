import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  RouterStateSnapshot
} from '@angular/router';
import { AuthService } from '../services/auth.service';
import { catchError, map, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private readonly authService: AuthService) { }

  canActivate(): Observable<boolean> {
    return this.authService.isLoggedIn().pipe(catchError(() => {
        this.authService.logout();
        return of(false);
      }),
      map(isValid => {
        if (!isValid) {
          this.authService.logout();
          return false;
        }
        return true;
      })
    );
  }

}
