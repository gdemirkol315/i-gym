import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EmployeesService, Employee } from '../employees.service';
import { AddEmployeeDialogComponent } from '../add-employee-dialog/add-employee-dialog.component';
import {first} from 'rxjs';

@Component({
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.scss']
})
export class EmployeeListComponent implements OnInit {
  employees: Employee[] = [];
  displayedColumns: string[] = ['name', 'lastName', 'email', 'address', 'position', 'role'];

  constructor(
    private employeesService: EmployeesService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(): void {
    this.employeesService.getEmployees().subscribe({
      next: (data) => {
        this.employees = data;
      },
      error: () => {
        this.snackBar.open('Error loading employees', 'Close', {
          duration: 3000,
          horizontalPosition: 'end'
        });
      }
    });
  }

  openAddEmployeeDialog(): void {
    const dialogRef = this.dialog.open(AddEmployeeDialogComponent, {
      width: '400px'
    });

    dialogRef.afterClosed().pipe(first()).subscribe(result => {
      if (result) {
        this.employeesService.createEmployee(result).subscribe({
          next: () => {
            this.loadEmployees();
            this.employeesService.toastr.success('Employee added successfully')
          },
          error: () => {
            this.employeesService.toastr.error('Error adding employee!','Error!')
          }
        });
      }
    });
  }
}
