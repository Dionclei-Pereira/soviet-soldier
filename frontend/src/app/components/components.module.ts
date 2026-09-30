import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage, UpperCasePipe } from '@angular/common';
import { AuthLoginComponent } from './auth/auth-login/auth-login.component';
import { AuthRegisterComponent } from './auth/auth-register/auth-register.component';
import { FormsModule } from '@angular/forms';
import { OptionsComponent } from './options/options.component';
import { TranslateModule, TranslatePipe } from '@ngx-translate/core';
import { provideTranslateHttpLoader } from '@ngx-translate/http-loader';
import { IndexComponent } from './home/index/index.component';
import { FormErrorsComponent } from './auth/form-errors/form-errors.component';
import { DirectivesModule } from '../directives/directives.module';

@NgModule({
  declarations: [
    AuthLoginComponent,
    AuthRegisterComponent,
    OptionsComponent,
    IndexComponent,
    FormErrorsComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    TranslatePipe,
    TranslateModule.forRoot(),
    UpperCasePipe,
    NgOptimizedImage,
    DirectivesModule
  ],
  providers: [
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
