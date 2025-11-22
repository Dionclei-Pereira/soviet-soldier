import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { IRegisterRequest } from '../interfaces/register-request.interface';
import { Observable, of, tap } from 'rxjs';
import { environment } from '../environment/environment.dev';
import { ILoginRequest } from '../interfaces/login-request.interface';
import { ILoginResponse } from '../interfaces/login-response.interface';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  token: string | null = null;

  constructor(private readonly http: HttpClient, private readonly router: Router) {}

  register(credentials: IRegisterRequest): Observable<void> {
    return this.http.post<void>(environment.apiUrl + 'auth/register', credentials);
  }

  login(credentials: ILoginRequest): Observable<ILoginResponse> {
    return this.http.post<ILoginResponse>(environment.apiUrl, credentials)
      .pipe(
        tap(response => {
          this.token = response.token;
        })
      );
  }

  logout(): void {
    this.token = null;
    this.router.navigate(['/auth']);
  }

  getToken(): string | null {
    return this.token;
  }

  isLoggedIn(): Observable<boolean> {
    const token = this.getToken();
    if (!token) return of(false);
    return this.http.get<boolean>(environment.apiUrl + 'auth/verify');
  }
}
