import { Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';

export const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () => import('./auth/auth.module').then(m => m.AuthModule)
  },
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: 'dashboard',
        loadChildren: () => import('./features/dashboard/dashboard.module')
          .then(m => m.DashboardModule)
      },
      {
        path: 'customers',
        loadChildren: () => import('./features/customer/customer.module')
          .then(m => m.CustomerModule)
      },
      {
        path: 'entries',
        loadChildren: () => import('./features/entry/entry.module')
          .then(m => m.EntryModule)
      },
      {
        path: 'products',
        loadChildren: () => import('./features/product/product.module')
          .then(m => m.ProductModule)
      },
      {
        path: 'profile',
        loadChildren: () => import('./features/profile/profile.module')
          .then(m => m.ProfileModule)
      },
      {
        path: 'employees',
        loadChildren: () => import('./features/employees/employees.module')
          .then(m => m.EmployeesModule)
      },
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      }
    ]
  }
];
