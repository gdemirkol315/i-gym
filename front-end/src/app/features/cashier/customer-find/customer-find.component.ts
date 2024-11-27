import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { CustomerService, Customer } from '../../customer/customer.service';
import { finalize, debounceTime, distinctUntilChanged, switchMap } from 'rxjs';
import { FormControl } from '@angular/forms';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';

@Component({
  selector: 'app-customer-find',
  templateUrl: './customer-find.component.html',
  styleUrls: ['./customer-find.component.scss']
})
export class CustomerFindComponent implements OnInit {
  @ViewChild('barcodeInput') barcodeInput!: ElementRef;
  
  searchType: 'barcode' | 'name' = 'barcode';
  barcode: string = '';
  loading: boolean = false;
  error: string | null = null;
  searchControl = new FormControl('');
  filteredCustomers: Customer[] = [];
  selectedCustomer: Customer | null = null;

  constructor(
    private dialogRef: MatDialogRef<CustomerFindComponent>,
    private customerService: CustomerService
  ) {}

  ngOnInit() {
    this.setupNameSearch();
  }

  ngAfterViewInit() {
    // Focus the input field when dialog opens
    setTimeout(() => {
      this.barcodeInput?.nativeElement?.focus();
    });
  }

  private setupNameSearch() {
    this.searchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(term => {
        if (typeof term === 'string' && term.length >= 2) {
          this.loading = true;
          return this.customerService.searchByName(term).pipe(
            finalize(() => this.loading = false)
          );
        }
        return [];
      })
    ).subscribe({
      next: (customers) => {
        this.filteredCustomers = customers;
        this.error = null;
      },
      error: (error) => {
        console.error('Error searching customers:', error);
        this.error = 'Error searching customers. Please try again.';
        this.filteredCustomers = [];
      }
    });
  }

  onBarcodeSubmit() {
    if (!this.barcode) {
      this.error = 'Please enter a barcode';
      return;
    }

    this.loading = true;
    this.error = null;

    this.customerService.checkBarcodeExists(this.barcode)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (exists) => {
          if (exists) {
            this.dialogRef.close(this.barcode);
          } else {
            this.error = 'Invalid barcode: Customer not found';
          }
        },
        error: (error) => {
          console.error('Error checking barcode:', error);
          this.error = 'Error checking barcode. Please try again.';
        }
      });
  }

  onCustomerSelected(event: MatAutocompleteSelectedEvent) {
    const customer = event.option.value as Customer;
    if (customer) {
      this.selectedCustomer = customer;
      this.error = null;
    }
  }

  onNameSubmit() {
    if (!this.selectedCustomer) {
      this.error = 'Please select a customer from the list';
      return;
    }

    if (!this.selectedCustomer.barcode) {
      this.error = 'Selected customer has no barcode';
      return;
    }

    this.dialogRef.close(this.selectedCustomer.barcode);
  }

  displayFn(customer: Customer | null): string {
    if (customer) {
      return `${customer.name} ${customer.lastName}`;
    }
    return '';
  }

  onCancel() {
    this.dialogRef.close();
  }
}
