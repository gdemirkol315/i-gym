import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { CreateEntryProductRequest, ProductService } from '../product.service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-add-entry-product-dialog',
  templateUrl: './add-entry-product-dialog.component.html',
  styleUrls: ['./add-entry-product-dialog.component.scss']
})
export class AddEntryProductDialogComponent {
  entryProductForm: FormGroup;
  isLoading = false;
  entryTypes = ['ONE_TIME', 'MULTI_PASS', 'SUBSCRIPTION'];

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<AddEntryProductDialogComponent>,
    private productService: ProductService,
  ) {
    this.entryProductForm = this.fb.group({
      name: ['', Validators.required],
      duration: [null],
      maxEntries: [null],
      price: ['', [Validators.required, Validators.min(0)]],
      entryType: ['', Validators.required],
      startDate: [null]
    });

    // Add validators based on entry type selection
    this.entryProductForm.get('entryType')?.valueChanges.subscribe(value => {
      const durationControl = this.entryProductForm.get('duration');
      const maxEntriesControl = this.entryProductForm.get('maxEntries');

      if (value === 'SUBSCRIPTION') {
        durationControl?.setValidators([Validators.required, Validators.min(1)]);
        maxEntriesControl?.setValidators(null);
      } else if (value === 'MULTI_PASS') {
        durationControl?.setValidators(null);
        maxEntriesControl?.setValidators([Validators.required, Validators.min(1)]);
      } else {
        durationControl?.setValidators(null);
        maxEntriesControl?.setValidators(null);
      }

      durationControl?.updateValueAndValidity();
      maxEntriesControl?.updateValueAndValidity();
    });
  }

  onSubmit(): void {
    if (this.entryProductForm.valid) {
      this.isLoading = true;
      const entryProductData: CreateEntryProductRequest = this.entryProductForm.value;

      this.productService.createEntryProduct(entryProductData)
        .pipe(finalize(() => this.isLoading = false))
        .subscribe({
          next: () => {
            this.productService.toastr.success('Entry product added successfully');
            this.dialogRef.close(true);
          },
          error: (error) => {
            this.productService.toastr.error('Failed to add entry product!');
          }
        });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
