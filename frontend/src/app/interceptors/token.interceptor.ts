import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { Observable } from "rxjs";


@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  constructor(private readonly authService: AuthService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    const token = this.authService.getToken();

    if (token) {
      const reqClone = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });

      return next.handle(reqClone);
    }

    return next.handle(req);
    }

}
