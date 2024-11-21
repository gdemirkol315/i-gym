import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ProductService, Product, EntryProduct } from '../product.service';
import { AddProductDialogComponent } from '../add-product-dialog/add-product-dialog.component';
import { AddEntryProductDialogComponent } from '../add-entry-product-dialog/add-entry-product-dialog.component';
import { AddStockDialogComponent } from '../add-stock-dialog/add-stock-dialog.component';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  entryProducts: EntryProduct[] = [];
  productColumns: string[] = ['id', 'name', 'barcode', 'category', 'price', 'quantity'];
  entryProductColumns: string[] = ['id', 'name', 'duration', 'maxEntries', 'price', 'entryType'];

  constructor(
    private productService: ProductService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.loadProducts();
    this.loadEntryProducts();
  }

  loadProducts() {
    this.productService.getProducts().subscribe(products => {
      this.products = products;
    });
  }

  loadEntryProducts() {
    this.productService.getEntryProducts().subscribe(products => {
      this.entryProducts = products;
    });
  }

  openAddProductDialog() {
    const dialogRef = this.dialog.open(AddProductDialogComponent);
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadProducts();
      }
    });
  }

  openAddEntryProductDialog() {
    const dialogRef = this.dialog.open(AddEntryProductDialogComponent);
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadEntryProducts();
      }
    });
  }

  openAddStockDialog() {
    const dialogRef = this.dialog.open(AddStockDialogComponent);
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadProducts();
      }
    });
  }
}
