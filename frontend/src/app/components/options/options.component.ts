import { AfterViewInit, Component, inject, signal } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-options',
  standalone: false,
  templateUrl: './options.component.html',
  styleUrl: './options.component.scss',
})
export class OptionsComponent implements AfterViewInit {
  currentFlag = signal<string>('');
  open = signal<boolean>(false);

  private readonly translateService = inject(TranslateService);

  ngAfterViewInit(): void {
    this.changeLang(this.getLang());
  }

  toggle(): void {
    this.open.update((val) => !val);
  }

  changeLang(language: string): void {
    this.translateService.use(language);
    this.currentFlag.set(`assets/icons/flag-${language}.svg`);
    localStorage.setItem('lang', language);
  }

  getLang(): string {
    const lang = localStorage.getItem('lang');
    if (lang) return lang;
    return 'en';
  }
}
