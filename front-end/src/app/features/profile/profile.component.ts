import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ChangePasswordDialogComponent } from './change-password-dialog.component';
import { ProfileService } from './profile.service';
import { Employee } from '../../core/models/employee.interface';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  employee: Employee | null = null;
  loading = true;
  error = '';
  isEditing = false;
  editedPhone = '';
  editedAddress = '';

  constructor(
    private profileService: ProfileService,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    this.loadProfile();
  }

  loadProfile() {
    this.loading = true;
    this.profileService.getProfile().subscribe({
      next: (data: Employee) => {
        this.employee = data;
        this.loading = false;
      },
      error: (err: any) => {
        this.error = 'Failed to load profile';
        this.loading = false;
      }
    });
  }

  openChangePasswordDialog() {
    const dialogRef = this.dialog.open(ChangePasswordDialogComponent, {
      width: '400px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'success') {
        this.profileService.toastr.info('Password changed successfully');
      } else {
        this.profileService.toastr.error('There was an error while changing the password!');
      }
    });
  }

  startEditing() {
    this.isEditing = true;
    this.editedPhone = this.employee?.phone || '';
    this.editedAddress = this.employee?.address || '';
  }

  saveChanges() {
    if (!this.employee) return;

    this.profileService.updateProfile(this.editedPhone, this.editedAddress).subscribe({
      next: (updatedEmployee: Employee) => {
        this.employee = updatedEmployee;
        this.profileService.toastr.info('Profile updated successfully');
        this.isEditing = false;
      },
      error: (err: any) => {
        this.profileService.toastr.error('There was an error updating profile!');
        this.error = 'Failed to update profile';
      }
    });
  }

  cancelEditing() {
    this.isEditing = false;
  }
}
