import { Component } from '@angular/core';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-options',
  standalone: false,
  templateUrl: './options.component.html',
  styleUrl: './options.component.scss',
})
export class OptionsComponent {

  currentFlag: string = '';
  open: boolean = false;

  constructor(private readonly translateService: TranslateService) {
    this.changeLang(this.getLang())
  }

  toggle(): void {
    this.open = !this.open;
  }

  changeLang(language: string): void {
    this.translateService.use(language);
    this.currentFlag = `assets/icons/flag-${language}.svg`
    localStorage.setItem('lang', language);
  }

  getLang(): string {
    const lang = localStorage.getItem('lang');
    if (lang) return lang;
    return 'en';
  }

}
