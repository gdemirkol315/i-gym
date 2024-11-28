import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../shared/shared.module';
import { EntryRoutingModule } from './entry-routing.module';
import { EntryManagementComponent } from './entry-management/entry-management.component';

@NgModule({
  declarations: [
    EntryManagementComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    EntryRoutingModule
  ]
})
export class EntryModule { }
