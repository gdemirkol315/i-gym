import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CashierService } from '../cashier.service';
import { EntryProduct, Product, ProductService } from '../../product/product.service';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { CustomerBarcodeDialogComponent } from '../customer-barcode-dialog/customer-barcode-dialog.component';

interface BasketItem {
  id: number;
  name: string;
  quantity: number;
  price: number;
  total: number;
  isEntryProduct?: boolean;
  customerBarcode?: string;
}

@Component({
  selector: 'app-cashier',
  templateUrl: './cashier.component.html',
  styleUrls: ['./cashier.component.scss']
})
export class CashierComponent implements OnInit, OnDestroy {
  productCategories: Map<String, Array<Product>> = new Map();
  entryProductCategories: Map<String, Array<EntryProduct>> = new Map();
  filteredProductCategories: Map<String, Array<Product>> = new Map();
  filteredEntryProductCategories: Map<String, Array<EntryProduct>> = new Map();
  basket: BasketItem[] = [];
  totalSum = 0;
  loading = true;
  error: string | null = null;
  searchTerm = '';
  private searchSubject = new Subject<string>();
  private barcodeBuffer = '';
  private barcodeTimeout: any;

  constructor(
    private cashierService: CashierService,
    private productService: ProductService,
    private dialog: MatDialog
  ) {
    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(term => {
      this.filterProducts(term);
    });
  }

  ngOnInit(): void {
    this.loadProducts();
    this.loadEntryProducts();
  }

  ngOnDestroy(): void {
    this.searchSubject.complete();
    if (this.barcodeTimeout) {
      clearTimeout(this.barcodeTimeout);
    }
  }

  @HostListener('document:keypress', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
    if (event.target instanceof HTMLInputElement) {
      return;
    }

    if (event.key === 'Enter') {
      if (this.barcodeBuffer.length >= 5) {
        this.processBarcode(this.barcodeBuffer);
      }
      this.barcodeBuffer = '';
      return;
    }

    if (this.barcodeTimeout) {
      clearTimeout(this.barcodeTimeout);
    }

    this.barcodeBuffer += event.key;

    this.barcodeTimeout = setTimeout(() => {
      if (this.barcodeBuffer.length >= 5) {
        this.processBarcode(this.barcodeBuffer);
      }
      this.barcodeBuffer = '';
    }, 50);
  }

  private processBarcode(barcode: string) {
    for (const products of this.productCategories.values()) {
      const product = products.find(p => p.barcode === barcode);
      if (product) {
        console.log('Found product:', product);
        this.addToBasket(product);
        break;
      }
    }
  }

  onSearch(term: string): void {
    this.searchTerm = term;
    this.searchSubject.next(term);
  }

  private filterProducts(term: string): void {
    this.filteredProductCategories.clear();
    this.filteredEntryProductCategories.clear();

    if (!term.trim()) {
      this.filteredProductCategories = new Map(this.productCategories);
      this.filteredEntryProductCategories = new Map(this.entryProductCategories);
      return;
    }

    const lowerTerm = term.toLowerCase();

    this.productCategories.forEach((products, category) => {
      const filteredProducts = products.filter(product =>
        product.name.toLowerCase().includes(lowerTerm) ||
        product.barcode?.toLowerCase().includes(lowerTerm)
      );

      if (filteredProducts.length > 0) {
        this.filteredProductCategories.set(category, filteredProducts);
      }
    });

    this.entryProductCategories.forEach((products, category) => {
      const filteredProducts = products.filter(product =>
        product.name.toLowerCase().includes(lowerTerm)
      );

      if (filteredProducts.length > 0) {
        this.filteredEntryProductCategories.set(category, filteredProducts);
      }
    });
  }

  private loadProducts(): void {
    this.productService.getProducts().subscribe({
      next: (data) => {
        data.forEach(item => {
          if (this.productCategories.get(item.category) !== undefined) {
            this.productCategories.get(item.category)?.push(item);
          } else {
            this.productCategories.set(item.category.toUpperCase(), new Array<Product>(item))
          }
        });
        this.filteredProductCategories = new Map(this.productCategories);
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading products:', error);
        this.error = 'Failed to load products';
        this.loading = false;
      }
    });
  }

  private loadEntryProducts(): void {
    this.productService.getEntryProducts().subscribe({
      next: (data) => {
        data.forEach(item => {
          if (this.entryProductCategories.get(item.entryType) !== undefined) {
            this.entryProductCategories.get(item.entryType)?.push(item);
          } else {
            this.entryProductCategories.set(item.entryType.toUpperCase(), new Array<EntryProduct>(item))
          }
        });
        this.filteredEntryProductCategories = new Map(this.entryProductCategories);
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading entry products:', error);
        this.error = 'Failed to load entry products';
        this.loading = false;
      }
    });
  }

  async addToBasket(item: any, isEntryProduct: boolean = false): Promise<void> {
    if (isEntryProduct) {
      const dialogRef = this.dialog.open(CustomerBarcodeDialogComponent, {
        width: '400px',
        disableClose: true
      });

      const customerBarcode = await dialogRef.afterClosed().toPromise();
      if (!customerBarcode) {
        return; // User cancelled or didn't provide barcode
      }

      // Add entry product with customer barcode
      const existingItem = this.basket.find(i => 
        i.name === item.name && 
        i.isEntryProduct && 
        i.customerBarcode === customerBarcode
      );

      if (existingItem) {
        existingItem.quantity++;
        existingItem.total = existingItem.quantity * existingItem.price;
      } else {
        this.basket.push({
          id: item.id,
          name: item.name,
          quantity: 1,
          price: item.price,
          total: item.price,
          isEntryProduct: true,
          customerBarcode: customerBarcode
        });
      }
    } else {
      // Regular product handling
      const existingItem = this.basket.find(i => i.id === item.id && !i.isEntryProduct);
      if (existingItem) {
        existingItem.quantity++;
        existingItem.total = existingItem.quantity * existingItem.price;
      } else {
        this.basket.push({
          id: item.id,
          name: item.name,
          quantity: 1,
          price: item.price,
          total: item.price,
          isEntryProduct: false
        });
      }
    }

    this.updateTotalSum();
  }

  removeFromBasket(itemId: number): void {
    const index = this.basket.findIndex(item => item.id === itemId);
    if (index !== -1) {
      this.basket.splice(index, 1);
      this.updateTotalSum();
    }
  }

  updateQuantity(itemId: number, increase: boolean): void {
    const item = this.basket.find(i => i.id === itemId);
    if (item) {
      if (increase) {
        item.quantity++;
      } else if (item.quantity > 1) {
        item.quantity--;
      }
      item.total = item.quantity * item.price;
      this.updateTotalSum();
    }
  }

  private updateTotalSum(): void {
    this.totalSum = this.basket.reduce((sum, item) => sum + item.total, 0);
  }
}
