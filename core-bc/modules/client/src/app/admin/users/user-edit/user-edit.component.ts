import { Component, OnInit } from '@angular/core';
import {Http} from '@angular/http';
import {ActivatedRoute} from '@angular/router';

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
      this.userId = params.userId;
    });
  }

}
