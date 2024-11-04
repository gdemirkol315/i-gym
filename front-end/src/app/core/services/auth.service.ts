import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';

interface LoginResponse {
  token: string;
  employeeId: number;
  name: string;
  role: string;
  position: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8080/api';
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  private userRoleSubject = new BehaviorSubject<string | null>(null);

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    this.checkInitialAuth();
  }

  get isAuthenticated$(): Observable<boolean> {
    return this.isAuthenticatedSubject.asObservable();
  }

  get userRole$(): Observable<string | null> {
    return this.userRoleSubject.asObservable();
  }

  private checkInitialAuth(): void {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('userRole');
    if (token) {
      this.isAuthenticatedSubject.next(true);
      this.userRoleSubject.next(role);
    }
  }

  login(username: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.API_URL}/auth/login`, {
      username,
      password
    }).pipe(
      tap(response => {
        localStorage.setItem('token', response.token);
        localStorage.setItem('userRole', response.role);
        localStorage.setItem('employeeId', response.employeeId.toString());
        this.isAuthenticatedSubject.next(true);
        this.userRoleSubject.next(response.role);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('userRole');
    localStorage.removeItem('employeeId');
    this.isAuthenticatedSubject.next(false);
    this.userRoleSubject.next(null);
    this.router.navigate(['/auth/login']);
  }

  isManager(): boolean {
    return localStorage.getItem('userRole') === 'MANAGER';
  }
}
