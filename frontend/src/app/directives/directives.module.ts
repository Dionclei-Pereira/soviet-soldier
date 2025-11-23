import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsernameValidatorDirective } from './username-validator.directive';



@NgModule({
  declarations: [
    UsernameValidatorDirective
  ],
  imports: [
    CommonModule
  ],
  exports: [
    UsernameValidatorDirective
  ]
})
export class DirectivesModule { }
