import {Location} from '@angular/common';
import {Component, OnInit} from '@angular/core';
import {Response} from '@angular/http';
import {Router} from '@angular/router';
import {ErrorHandler} from '../../../shared/error-handler';
import {User} from '../../../model/user';
import {Prodn1} from '../../../model/prodn1';
import {JwtHttp} from '../../../core/jwt-http';
import {AuthService} from '../../../core/auth/auth.service';

@Component({
  selector: 'app-users-list',
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.scss']
})
export class UsersListComponent implements OnInit {

  users: User[];

  constructor(private http: JwtHttp,
              private errorHandler: ErrorHandler,
              private authService: AuthService,
              private router: Router) { }

  ngOnInit() {
    this.updateUsers();
  }

  private updateUsers() {
    this.http.get('/api/user').map<Response, User[]>(response => response.json())
      .subscribe(
        users => this.users = users
      );
  }

  confirmDeleteTODO(user: User) {
    this.http.delete('/api/user/' + user.id)
      .subscribe(response => {
        this.updateUsers();
      });
  }

  prodn1sToString(prodn1: Prodn1[]) {
    return prodn1.map(prodn1 => prodn1.producentid).join(', ')
  }

  impersonate(user: User) {
    this.http.post('/api/login/impersonate', user).subscribe(response => {
      this.authService.jwt = response.text();
      this.router.navigate(['/']);
    });
  }

}
