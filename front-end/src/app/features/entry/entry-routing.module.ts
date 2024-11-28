import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EntryManagementComponent } from './entry-management/entry-management.component';

const routes: Routes = [
  {
    path: 'entry-management',
    component: EntryManagementComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EntryRoutingModule { }
