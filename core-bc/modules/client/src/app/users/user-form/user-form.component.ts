import {Component, Input, OnInit} from '@angular/core';
import {ErrorHandler} from "../../shared/error-handler";
import {MdSnackBar} from "@angular/material";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Http} from "@angular/http";
import {User} from "../../model/user";

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.scss']
})
export class UserFormComponent implements OnInit {

  @Input('userId') userId;

  userForm: FormGroup;

  user: User;

  constructor(private http: Http,
              private formBuilder: FormBuilder,
              private snackBar: MdSnackBar,
              private errorHandler: ErrorHandler) { }

  ngOnInit() {

    if (this.userId) {

    } else {
      this.user = new User();
    }

    this.buildForm();
  }

  private buildForm() {

    this.userForm = this.formBuilder.group({
      'userId': [{value: this.user.id, disabled: false}, [Validators.required]]
    });

  }

}
