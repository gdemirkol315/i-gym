import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { CreateEmployeeRequest } from '../employees.service';

@Component({
  selector: 'app-add-employee-dialog',
  templateUrl: './add-employee-dialog.component.html',
  styleUrls: ['./add-employee-dialog.component.scss']
})
export class AddEmployeeDialogComponent {
  employeeForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<AddEmployeeDialogComponent>
  ) {
    this.employeeForm = this.fb.group({
      name: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      address: ['', Validators.required],
      position: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.employeeForm.valid) {
      const employeeData: CreateEmployeeRequest = this.employeeForm.value;
      this.dialogRef.close(employeeData);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
