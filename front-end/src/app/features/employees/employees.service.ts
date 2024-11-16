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

@Injectable({
  providedIn: 'root'
})
export class EmployeesService extends DataService {
  private readonly API_EXTENSION = 'employee';

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
}
