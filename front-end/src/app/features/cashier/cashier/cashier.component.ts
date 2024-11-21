import {Component, OnInit} from '@angular/core';
import {CashierService} from '../cashier.service';
import {EntryProduct, Product, ProductService} from '../../product/product.service';

interface BasketItem {
  id: number;
  name: string;
  quantity: number;
  price: number;
  total: number;
}

@Component({
  selector: 'app-cashier',
  templateUrl: './cashier.component.html',
  styleUrls: ['./cashier.component.scss']
})
export class CashierComponent implements OnInit {
  productCategories: Map<String, Array<Product>> = new Map();
  entryProductCategories: Map<String, Array<EntryProduct>> = new Map();
  basket: BasketItem[] = [];
  totalSum = 0;
  loading = true;
  error: string | null = null;

  constructor(private cashierService: CashierService,
              private productService: ProductService) {
  }

  ngOnInit(): void {
    this.loadProducts();
    this.loadEntryProducts();
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

        })
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

        })
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading entry products:', error);
        this.error = 'Failed to load entry products';
        this.loading = false;
      }
    });
  }

  addToBasket(item: any, isEntryProduct: boolean = false): void {
    var existingItem;
    if (!isEntryProduct) {
      existingItem = this.basket.find(i => i.id === item.id);
    } else {
      existingItem = this.basket.find(i => i.name === item.name);
    }
    if (existingItem) {
      existingItem.quantity++;
      existingItem.total = existingItem.quantity * existingItem.price;
    } else {
      this.basket.push({
        id: item.id,
        name: item.name,
        quantity: 1,
        price: item.price,
        total: item.price
      });
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
