import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { ProductService, Product, AddStockRequest } from '../product.service';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';

@Component({
  selector: 'app-add-stock-dialog',
  templateUrl: './add-stock-dialog.component.html',
  styleUrls: ['./add-stock-dialog.component.scss']
})
export class AddStockDialogComponent implements OnInit {
  form: FormGroup;
  products: Product[] = [];
  filteredProducts: Observable<Product[]> = new Observable<Product[]>();

  constructor(
    private dialogRef: MatDialogRef<AddStockDialogComponent>,
    private productService: ProductService
  ) {
    this.form = new FormGroup({
      product: new FormControl<Product | string>('', Validators.required),
      quantity: new FormControl<number>(1, [Validators.required, Validators.min(1)]),
      notes: new FormControl<string>('', Validators.required),
      employeeId: new FormControl<number>(1, Validators.required) // TODO: Get actual logged-in employee ID
    });
  }

  ngOnInit() {
    this.productService.getProducts().subscribe(products => {
      this.products = products;
      this.setupProductFilter();
    });
  }

  private setupProductFilter() {
    const productControl = this.form.get('product');
    if (productControl) {
      this.filteredProducts = productControl.valueChanges.pipe(
        startWith(''),
        map(value => {
          const name = typeof value === 'string' ? value : value?.name;
          return name ? this._filter(name) : this.products.slice();
        })
      );
    }
  }

  private _filter(value: string): Product[] {
    const filterValue = value.toLowerCase();
    return this.products.filter(product => 
      product.name.toLowerCase().includes(filterValue) || 
      (product.barcode && product.barcode.toLowerCase().includes(filterValue))
    );
  }

  displayFn(product: Product): string {
    return product && product.name ? product.name : '';
  }

  onSubmit() {
    if (this.form.valid) {
      const formValue = this.form.value;
      const product = formValue.product as Product;
      
      const request: AddStockRequest = {
        productId: product.id,
        quantity: formValue.quantity,
        notes: formValue.notes,
        employeeId: formValue.employeeId
      };

      this.productService.addStock(request)
        .subscribe(() => {
          this.dialogRef.close(true);
        });
    }
  }

  onCancel() {
    this.dialogRef.close();
  }
}
