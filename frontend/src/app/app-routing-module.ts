import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthLoginComponent } from './components/auth/auth-login/auth-login.component';
import { AuthRegisterComponent } from './components/auth/auth-register/auth-register.component';

const routes: Routes = [
  {
    path: 'auth',
    children: [
      { path: 'login', component: AuthLoginComponent },
      { path: 'register', component: AuthRegisterComponent },
      { path: '', redirectTo: 'login', pathMatch: 'full' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
