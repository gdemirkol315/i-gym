import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { DataService } from '../../core/data/data.service';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

interface Product {
  id: number;
  name: string;
  price: number;
  category: string;
}

interface CategoryGroup {
  name: string;
  products: Product[];
}

@Injectable({
  providedIn: 'root'
})
export class CashierService extends DataService {


  private groupByCategory(products: Product[]): CategoryGroup[] {
    const grouped = products.reduce((acc, product) => {
      const category = product.category || 'Uncategorized';
      if (!acc[category]) {
        acc[category] = [];
      }
      acc[category].push(product);
      return acc;
    }, {} as { [key: string]: Product[] });

    return Object.entries(grouped).map(([name, products]) => ({
      name,
      products: products.sort((a, b) => a.name.localeCompare(b.name))
    }));
  }

  saveSale(saleData: any): Observable<any> {
    return this.http.post(`${this.hostname}/sales`, saleData);
  }
}
