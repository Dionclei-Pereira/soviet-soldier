import { Component, inject, signal, OnInit } from '@angular/core';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-index',
  standalone: false,
  templateUrl: './index.component.html',
  styleUrl: './index.component.scss',
})
export class IndexComponent implements OnInit {
  token = signal<string | null>(null);

  private readonly authService = inject(AuthService);

  ngOnInit(): void {
    this.token.set(this.authService.getToken());
  }
}
