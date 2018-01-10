import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../core/auth/auth.service";

@Component({
  selector: 'app-admin-landing',
  templateUrl: './admin-landing.component.html',
  styleUrls: ['./admin-landing.component.scss']
})
export class AdminLandingComponent implements OnInit {

  constructor(private authService: AuthService) {}

  ngOnInit() {
  }

  get admin() {
    return this.authService.isAdmin();
  }

  get loggedIn() {
    return this.authService.isAuthenticated();
  }
}
