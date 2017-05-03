import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Http} from '@angular/http';
import {Data} from '../../model/data';
import {AuthService} from "../../core/auth/auth.service";

@Component({
  selector: 'app-apk-detail',
  templateUrl: './apk-detail.component.html',
  styleUrls: ['./apk-detail.component.css']
})
export class ApkDetailComponent implements OnInit {

  id: string;
  data: Data;

  constructor(protected route: ActivatedRoute,
              protected http: Http,
              protected authService: AuthService) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      console.log(params);
      this.id = params.id;

      if (this.id) {
        this.http.get('/api/data/' + this.id)
          .map(response => response.json())
          .subscribe((data: Data) => {
            this.data = data;
          });
      }
    });
  }

  protected getId() {
    return this.id;
  }

  userHasEditPermission(data: Data) {
    return this.authService.userHasDataEditPermission(data);
  }
}
