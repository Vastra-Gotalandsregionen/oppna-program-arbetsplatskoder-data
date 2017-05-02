import { Component, OnInit } from '@angular/core';
import {Http} from "@angular/http";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.scss']
})
export class UserEditComponent implements OnInit {

  userId: string;

  constructor(protected route: ActivatedRoute,
              protected http: Http) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      console.log(params);
      this.userId = params.userId;

      /*if (this.userId) {
        this.http.get('/api/user/' + this.userId)
          .map(response => response.json())
          .subscribe((user: User) => {
            this.data = data;
          });
      }*/
    });
  }

}
