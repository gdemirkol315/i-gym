import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-customer-barcode-dialog',
  template: `
    <h2 mat-dialog-title>Enter Customer Barcode</h2>
    <mat-dialog-content>
      <mat-form-field appearance="outline" class="full-width">
        <mat-label>Customer Barcode</mat-label>
        <input matInput [(ngModel)]="barcode" placeholder="Scan or enter customer barcode" 
               (keyup.enter)="onSubmit()" #barcodeInput>
      </mat-form-field>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button (click)="onCancel()">Cancel</button>
      <button mat-raised-button color="primary" (click)="onSubmit()">Submit</button>
    </mat-dialog-actions>
  `,
  styles: [`
    .full-width {
      width: 100%;
    }
  `]
})
export class CustomerBarcodeDialogComponent {
  barcode: string = '';

  constructor(private dialogRef: MatDialogRef<CustomerBarcodeDialogComponent>) {}

  ngAfterViewInit() {
    // Focus the input field when dialog opens
    setTimeout(() => {
      document.querySelector('input')?.focus();
    });
  }

  onSubmit() {
    if (this.barcode) {
      this.dialogRef.close(this.barcode);
    }
  }

  onCancel() {
    this.dialogRef.close();
  }
}
