import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DataService } from '../../core/data/data.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

export interface Customer {
  id: number;
  name: string;
  lastName: string;
  email: string;
  birthDate: string;
  barcode: string;
}

export interface CreateCustomerRequest {
  name: string;
  lastName: string;
  email: string;
  birthDate: string;
  barcode: string;
}

@Injectable({
  providedIn: 'root'
})
export class CustomerService extends DataService {
  private readonly API_EXTENSION = 'customer';

  constructor(
    http: HttpClient,
    toastr: ToastrService,
    router: Router
  ) {
    super(http, toastr, router);
  }

  getCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(this.hostname + this.API_EXTENSION + "/list");
  }

  createCustomer(customer: CreateCustomerRequest): Observable<Customer> {
    return this.http.post<Customer>(this.hostname + this.API_EXTENSION + "/create", customer);
  }
}
