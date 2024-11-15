import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { ProfileService } from './profile.service';

@Component({
  selector: 'app-change-password-dialog',
  template: `
    <h2 mat-dialog-title>Change Password</h2>
    <mat-dialog-content>
      <form [formGroup]="passwordForm">
        <mat-form-field appearance="fill" class="full-width">
          <mat-label>Current Password</mat-label>
          <input matInput type="password" formControlName="oldPassword">
          <mat-error *ngIf="passwordForm.get('oldPassword')?.errors?.['required']">
            Current password is required
          </mat-error>
        </mat-form-field>

        <mat-form-field appearance="fill" class="full-width">
          <mat-label>New Password</mat-label>
          <input matInput type="password" formControlName="newPassword">
          <mat-error *ngIf="passwordForm.get('newPassword')?.errors?.['required']">
            New password is required
          </mat-error>
        </mat-form-field>

        <mat-form-field appearance="fill" class="full-width">
          <mat-label>Confirm New Password</mat-label>
          <input matInput type="password" formControlName="confirmPassword">
          <mat-error *ngIf="passwordForm.get('confirmPassword')?.errors?.['required']">
            Password confirmation is required
          </mat-error>
          <mat-error *ngIf="passwordForm.errors?.['passwordMismatch']">
            Passwords do not match
          </mat-error>
        </mat-form-field>
      </form>
      <mat-error *ngIf="error">{{ error }}</mat-error>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button mat-dialog-close>Cancel</button>
      <button mat-raised-button color="primary" 
              [disabled]="passwordForm.invalid || loading"
              (click)="changePassword()">
        Change Password
      </button>
    </mat-dialog-actions>
  `,
  styles: [`
    .full-width {
      width: 100%;
      margin-bottom: 15px;
    }
    mat-dialog-content {
      min-width: 350px;
    }
  `]
})
export class ChangePasswordDialogComponent {
  passwordForm: FormGroup;
  loading = false;
  error = '';

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<ChangePasswordDialogComponent>,
    private profileService: ProfileService
  ) {
    this.passwordForm = this.fb.group({
      oldPassword: ['', Validators.required],
      newPassword: ['', Validators.required],
      confirmPassword: ['', Validators.required]
    }, { validators: this.passwordMatchValidator });
  }

  passwordMatchValidator(group: FormGroup) {
    const newPassword = group.get('newPassword')?.value;
    const confirmPassword = group.get('confirmPassword')?.value;
    return newPassword === confirmPassword ? null : { passwordMismatch: true };
  }

  changePassword() {
    if (this.passwordForm.valid) {
      this.loading = true;
      this.error = '';
      
      this.profileService.changePassword(
        this.passwordForm.get('oldPassword')?.value,
        this.passwordForm.get('newPassword')?.value
      ).subscribe({
        next: () => {
          this.dialogRef.close('success');
        },
        error: (err) => {
          this.error = err.error || 'Failed to change password';
          this.loading = false;
        }
      });
    }
  }
}
