import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Employee {
  id: number;
  name: string;
  lastName?: string;
  email: string;
  address?: string;
  position: string;
  role: string;
}

export interface CreateEmployeeRequest {
  name: string;
  lastName?: string;
  email: string;
  address?: string;
  position: string;
}

@Injectable({
  providedIn: 'root'
})
export class EmployeesService {
  private readonly API_URL = 'http://localhost:8080/api/employee';

  constructor(private http: HttpClient) {}

  getEmployees(): Observable<Employee[]> {
    return this.http.get<Employee[]>(this.API_URL);
  }

  createEmployee(employee: CreateEmployeeRequest): Observable<Employee> {
    return this.http.post<Employee>(this.API_URL, employee);
  }
}
