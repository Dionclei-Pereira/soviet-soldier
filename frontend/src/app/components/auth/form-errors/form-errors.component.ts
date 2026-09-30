import { Component, input, Input } from '@angular/core';
import { NgModel } from '@angular/forms';

@Component({
  selector: 'app-form-errors',
  standalone: false,
  templateUrl: './form-errors.component.html',
  styleUrl: './form-errors.component.scss',
})
export class FormErrorsComponent {
  model = input.required<NgModel>();

  name = input.required<string>();
}
