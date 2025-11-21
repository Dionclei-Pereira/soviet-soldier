import {NgModule} from '@angular/core';
import {CommonModule, NgOptimizedImage, UpperCasePipe} from '@angular/common';
import {AuthLoginComponent} from './auth/auth-login/auth-login.component';
import {AuthRegisterComponent} from './auth/auth-register/auth-register.component';
import {FormsModule} from '@angular/forms';
import {OptionsComponent} from './options/options.component';
import {TranslateModule, TranslatePipe} from '@ngx-translate/core';
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import {provideTranslateHttpLoader} from '@ngx-translate/http-loader';

@NgModule({
  declarations: [
    AuthLoginComponent,
    AuthRegisterComponent,
    OptionsComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    TranslatePipe,
    TranslateModule.forRoot(),
    UpperCasePipe,
    NgOptimizedImage
  ],
  providers: [
    provideHttpClient(withInterceptorsFromDi()),
    provideTranslateHttpLoader({
      prefix: './assets/i18n/',
      suffix: '.json'
    })
  ],
  exports: [
    AuthLoginComponent,
    AuthRegisterComponent,
    OptionsComponent
  ]
})
export class ComponentsModule {
}
