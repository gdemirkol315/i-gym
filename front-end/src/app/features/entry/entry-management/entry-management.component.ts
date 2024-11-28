import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ProductService, EntryProduct } from '../../product/product.service';
import { AddEntryProductDialogComponent } from '../../product/add-entry-product-dialog/add-entry-product-dialog.component';

@Component({
  selector: 'app-entry-management',
  templateUrl: './entry-management.component.html',
  styleUrls: ['./entry-management.component.scss']
})
export class EntryManagementComponent implements OnInit {
  entryProducts: EntryProduct[] = [];
  entryProductColumns: string[] = ['id', 'name', 'duration', 'maxEntries', 'price', 'entryType'];

  constructor(
    private productService: ProductService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.loadEntryProducts();
  }

  loadEntryProducts() {
    this.productService.getEntryProducts().subscribe(products => {
      this.entryProducts = products;
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
}
