import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { EmployeesService, Employee } from '../employees.service';
import { AddEmployeeDialogComponent } from '../add-employee-dialog/add-employee-dialog.component';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.scss']
})
export class EmployeeListComponent implements OnInit {
  employees: Employee[] = [];
  displayedColumns: string[] = ['name', 'lastName', 'email', 'phone', 'address', 'role'];
  editingRole: { [key: number]: boolean } = {};
  availableRoles: string[];

  constructor(
    private employeesService: EmployeesService,
    private dialog: MatDialog,
    private toastr: ToastrService
  ) {
    this.availableRoles = this.employeesService.ROLES;
  }

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(): void {
    this.employeesService.getEmployees().subscribe({
      next: (data) => {
        this.employees = data;
      },
      error: () => {
        this.toastr.error('Error loading employees');
      }
    });
  }

  openAddEmployeeDialog(): void {
    const dialogRef = this.dialog.open(AddEmployeeDialogComponent, {
      width: '400px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadEmployees();
      }
    });
  }

  startEditingRole(employeeId: number): void {
    // Reset any other editing states
    Object.keys(this.editingRole).forEach(key => {
      this.editingRole[Number(key)] = false;
    });
    // Set current employee to editing mode
    this.editingRole[employeeId] = true;
  }

  saveRole(employee: Employee, newRole: string): void {
    if (employee.role === newRole) {
      this.editingRole[employee.id] = false;
      return;
    }

    this.employeesService.updateRole({ employeeId: employee.id, role: newRole }).subscribe({
      next: (updatedEmployee) => {
        const index = this.employees.findIndex(e => e.id === updatedEmployee.id);
        if (index !== -1) {
          this.employees[index] = updatedEmployee;
        }
        this.editingRole[employee.id] = false;
        this.toastr.success('Role updated successfully');
      },
      error: () => {
        this.toastr.error('Error updating role');
      }
    });
  }

  cancelEditingRole(employeeId: number): void {
    this.editingRole[employeeId] = false;
  }
}
