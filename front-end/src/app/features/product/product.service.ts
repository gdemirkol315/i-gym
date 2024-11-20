import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DataService } from '../../core/data/data.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

export interface Product {
  id: number;
  name: string;
  category: string;
  price: number;
  quantity: number;
  barcode?: string;
}

export interface CreateProductRequest {
  name: string;
  category: string;
  price: number;
  quantity: number;
  barcode?: string;
}

export interface EntryProduct {
  id: number;
  name: string;
  duration: number | null;
  maxEntries: number | null;
  price: number;
  entryType: 'ONE_TIME' | 'MULTI_PASS' | 'SUBSCRIPTION';
}

export interface CreateEntryProductRequest {
  name: string;
  duration: number | null;
  maxEntries: number | null;
  price: number;
  entryType: 'ONE_TIME' | 'MULTI_PASS' | 'SUBSCRIPTION';
}

@Injectable({
  providedIn: 'root'
})
export class ProductService extends DataService {
  private readonly API_EXTENSION = 'product';

  constructor(
    http: HttpClient,
    toastr: ToastrService,
    router: Router
  ) {
    super(http, toastr, router);
  }

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.hostname + this.API_EXTENSION + "/list");
  }

  createProduct(product: CreateProductRequest): Observable<Product> {
    return this.http.post<Product>(this.hostname + this.API_EXTENSION + "/create", product);
  }

  getEntryProducts(): Observable<EntryProduct[]> {
    return this.http.get<EntryProduct[]>(this.hostname + this.API_EXTENSION + "/entry/list");
  }

  createEntryProduct(entryProduct: CreateEntryProductRequest): Observable<EntryProduct> {
    return this.http.post<EntryProduct>(this.hostname + this.API_EXTENSION + "/entry/create", entryProduct);
  }
}
