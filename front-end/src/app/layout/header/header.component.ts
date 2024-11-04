import { Component, EventEmitter, Output } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  @Output() toggleSidenav = new EventEmitter<void>();

  constructor(private router: Router) {}

  logout(): void {
    // TODO: Implement logout logic with AuthService
    localStorage.removeItem('token');
    this.router.navigate(['/auth/login']);
  }
}
