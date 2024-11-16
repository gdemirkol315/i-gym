import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environment";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";

@Injectable({providedIn: "root"})
export class DataService {

  private _hostname = environment.apiURL;

  constructor(private _http: HttpClient, private _toastr: ToastrService, private _router:Router) {
  }



  get hostname(): string {
    return this._hostname;
  }


  get http(): HttpClient {
    return this._http;
  }

  set http(value: HttpClient) {
    this._http = value;
  }

  get toastr(): ToastrService {
    return this._toastr;
  }


  get router(): Router {
    return this._router;
  }

  set router(value: Router) {
    this._router = value;
  }
}
