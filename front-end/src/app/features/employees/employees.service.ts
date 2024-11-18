import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DataService } from '../../core/data/data.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

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

export interface UpdateRoleRequest {
  employeeId: number;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class EmployeesService extends DataService {
  private readonly API_EXTENSION = 'employee';
  readonly ROLES = ['MANAGER', 'EMPLOYEE', 'SUPERVISOR'];

  constructor(
    http: HttpClient,
    toastr: ToastrService,
    router: Router
  ) {
    super(http, toastr, router);
  }

  getEmployees(): Observable<Employee[]> {
    return this.http.get<Employee[]>(this.hostname + this.API_EXTENSION + "/list");
  }

  createEmployee(employee: CreateEmployeeRequest): Observable<Employee> {
    return this.http.post<Employee>(this.hostname + this.API_EXTENSION + "/create", employee);
  }

  updateRole(request: UpdateRoleRequest): Observable<Employee> {
    return this.http.put<Employee>(this.hostname + this.API_EXTENSION + "/update-role", request);
  }
}
