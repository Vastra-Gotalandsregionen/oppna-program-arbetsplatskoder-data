import { Component, OnInit } from '@angular/core';
import {StateService} from '../core/state/state.service';
import {AuthService} from '../core/auth/auth.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {

  constructor(private authService: AuthService,
              private stateService: StateService) {}

  ngOnInit() {
  }

  get admin() {
    return this.authService.getLoggedInRole() === 'ADMIN';
  }


}
