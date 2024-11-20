import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '../../shared/shared.module';
import { ProductRoutingModule } from './product-routing.module';

import { ProductListComponent } from './product-list/product-list.component';
import { AddProductDialogComponent } from './add-product-dialog/add-product-dialog.component';
import { AddEntryProductDialogComponent } from './add-entry-product-dialog/add-entry-product-dialog.component';

@NgModule({
  declarations: [
    ProductListComponent,
    AddProductDialogComponent,
    AddEntryProductDialogComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SharedModule,
    ProductRoutingModule
  ]
})
export class ProductModule { }
