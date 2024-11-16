import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { CreateEmployeeRequest, EmployeesService } from '../employees.service';
import { finalize } from 'rxjs';

enum EmployeeRole {
  MANAGER = 'MANAGER',
  EMPLOYEE = 'EMPLOYEE',
  SUPERVISOR = 'SUPERVISOR'
}

@Component({
  selector: 'app-add-employee-dialog',
  templateUrl: './add-employee-dialog.component.html',
  styleUrls: ['./add-employee-dialog.component.scss']
})
export class AddEmployeeDialogComponent {
  employeeForm: FormGroup;
  isLoading = false;
  roles = Object.values(EmployeeRole);

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<AddEmployeeDialogComponent>,
    private employeesService: EmployeesService,
  ) {
    this.employeeForm = this.fb.group({
      name: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      address: ['', ],
      role: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.employeeForm.valid) {
      this.isLoading = true;
      const employeeData: CreateEmployeeRequest = this.employeeForm.value;

      this.employeesService.createEmployee(employeeData)
        .pipe(finalize(() => this.isLoading = false))
        .subscribe({
          next: () => {
            this.employeesService.toastr.success('Employee added successfully');
            this.dialogRef.close(true);
          },
          error: (error) => {
            this.employeesService.toastr.error('Failed to add employee! Email might already be in use!');
          }
        });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
