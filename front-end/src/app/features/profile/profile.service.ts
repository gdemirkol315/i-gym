import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Employee } from '../../core/models/employee.interface';
import {DataService} from '../../core/data/data.service';

@Injectable({
  providedIn: 'root'
})
export class ProfileService extends DataService {
  private apiExtension = 'employee';

  getProfile(): Observable<Employee> {
    return this.http.get<Employee>(`${this.http + this.apiExtension}/profile`);
  }

  changePassword(oldPassword: string, newPassword: string): Observable<void> {
    return this.http.post<void>(`${ this.http + this.apiExtension}/change-password`, {
      oldPassword,
      newPassword
    });
  }

  updateProfile(phone: string, address: string): Observable<Employee> {
    return this.http.post<Employee>(`${this.http + this.apiExtension}/update-profile`, {
      phone,
      address
    });
  }
}
