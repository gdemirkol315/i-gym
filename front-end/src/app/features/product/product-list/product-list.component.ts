import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ProductService, Product, EntryProduct } from '../product.service';
import { AddProductDialogComponent } from '../add-product-dialog/add-product-dialog.component';
import { AddEntryProductDialogComponent } from '../add-entry-product-dialog/add-entry-product-dialog.component';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  entryProducts: EntryProduct[] = [];

  productColumns: string[] = ['id', 'name', 'category', 'price', 'quantity'];
  entryProductColumns: string[] = ['id', 'name', 'duration', 'maxEntries', 'price', 'entryType'];

  constructor(
    private productService: ProductService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadProducts();
    this.loadEntryProducts();
  }

  loadProducts(): void {
    this.productService.getProducts().subscribe({
      next: (data) => {
        this.products = data;
      },
      error: () => {
        this.productService.toastr.error('Error loading products');
      }
    });
  }

  loadEntryProducts(): void {
    this.productService.getEntryProducts().subscribe({
      next: (data) => {
        this.entryProducts = data;
      },
      error: () => {
        this.productService.toastr.error('Error loading entry products');
      }
    });
  }

  openAddProductDialog(): void {
    const dialogRef = this.dialog.open(AddProductDialogComponent, {
      width: '400px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadProducts();
      }
    });
  }

  openAddEntryProductDialog(): void {
    const dialogRef = this.dialog.open(AddEntryProductDialogComponent, {
      width: '400px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadEntryProducts();
      }
    });
  }
}
