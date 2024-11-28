import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ProductService, Product } from '../product.service';
import { AddProductDialogComponent } from '../add-product-dialog/add-product-dialog.component';
import { AddStockDialogComponent } from '../add-stock-dialog/add-stock-dialog.component';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  productColumns: string[] = ['id', 'name', 'barcode', 'category', 'price', 'quantity'];

  constructor(
    private productService: ProductService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts() {
    this.productService.getProducts().subscribe(products => {
      this.products = products;
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

  openAddStockDialog() {
    const dialogRef = this.dialog.open(AddStockDialogComponent);
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadProducts();
      }
    });
  }
}
