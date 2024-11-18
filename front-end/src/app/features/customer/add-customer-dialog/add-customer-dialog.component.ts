import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { CreateCustomerRequest, CustomerService } from '../customer.service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-add-customer-dialog',
  templateUrl: './add-customer-dialog.component.html',
  styleUrls: ['./add-customer-dialog.component.scss']
})
export class AddCustomerDialogComponent {
  customerForm: FormGroup;
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<AddCustomerDialogComponent>,
    private customerService: CustomerService,
  ) {
    this.customerForm = this.fb.group({
      name: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      birthDate: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.customerForm.valid) {
      this.isLoading = true;
      const customerData: CreateCustomerRequest = this.customerForm.value;

      this.customerService.createCustomer(customerData)
        .pipe(finalize(() => this.isLoading = false))
        .subscribe({
          next: () => {
            this.customerService.toastr.success('Customer added successfully');
            this.dialogRef.close(true);
          },
          error: (error) => {
            this.customerService.toastr.error('Failed to add customer! Email might already be in use!');
          }
        });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
