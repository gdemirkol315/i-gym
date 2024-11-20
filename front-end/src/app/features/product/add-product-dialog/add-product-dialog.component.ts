import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { CreateProductRequest, ProductService } from '../product.service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-add-product-dialog',
  templateUrl: './add-product-dialog.component.html',
  styleUrls: ['./add-product-dialog.component.scss']
})
export class AddProductDialogComponent {
  productForm: FormGroup;
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<AddProductDialogComponent>,
    private productService: ProductService,
  ) {
    this.productForm = this.fb.group({
      name: ['', Validators.required],
      barcode: [''],
      category: ['', Validators.required],
      price: ['', [Validators.required, Validators.min(0)]],
      quantity: ['', [Validators.required, Validators.min(0)]]
    });
  }

  onSubmit(): void {
    if (this.productForm.valid) {
      this.isLoading = true;
      const productData: CreateProductRequest = this.productForm.value;

      this.productService.createProduct(productData)
        .pipe(finalize(() => this.isLoading = false))
        .subscribe({
          next: () => {
            this.productService.toastr.success('Product added successfully');
            this.dialogRef.close(true);
          },
          error: (error) => {
            this.productService.toastr.error('Failed to add product!');
          }
        });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
