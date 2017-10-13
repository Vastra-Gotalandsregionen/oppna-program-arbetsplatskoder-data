import {Component, OnInit} from '@angular/core';
import {Response} from '@angular/http';
import {Router} from '@angular/router';
import {ErrorHandler} from '../../../shared/error-handler';
import {User} from '../../../model/user';
import {Prodn1} from '../../../model/prodn1';
import {JwtHttp} from '../../../core/jwt-http';
import {AuthService} from '../../../core/auth/auth.service';

import {MatDialog} from "@angular/material/dialog";
import {ConfirmDialogComponent} from "../../../shared/confirm-dialog/confirm-dialog.component";

@Component({
  selector: 'app-users-list',
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.scss']
})
export class UsersListComponent implements OnInit {

  users: User[];

  usersWithoutData: string[] = [];

  constructor(private http: JwtHttp,
              private errorHandler: ErrorHandler,
              private authService: AuthService,
              private router: Router,
              private dialog: MatDialog) { }

  ngOnInit() {
    this.updateUsers();
    this.http.get('/api/data/users').map<Response, string[]>(response => response.json())
    .subscribe(value => this.usersWithoutData = value);
  }

  private updateUsers() {
    this.http.get('/api/user').map<Response, User[]>(response => response.json())
      .subscribe(
        users => this.users = users
      );
  }

/*
  confirmDeleteTODO(user: User) {
    this.http.delete('/api/user/' + user.id)
      .subscribe(response => {
        this.updateUsers();
      });
  }
*/
  confirmInactivateTODO(user: User) {
    user.inactivated = true;
    this.http.delete('/api/user/' + user.id)
      .subscribe(response => {
        this.updateUsers();
      });
  }

  prodn1sToString(prodn1: Prodn1[]) {
    return prodn1.map(prodn1 => prodn1.kortnamn).join(', ')
  }

  count(prodn1s: Prodn1[]) {
    return prodn1s.length;
  }

  impersonate(user: User) {
    this.http.post('/api/login/impersonate', user).subscribe(response => {
      this.authService.jwt = response.text();
      this.router.navigate(['/']);
    });
  }

  confirmDelete(user: User) {
    let dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        text: 'Är du säker att du vill ta bort vald användare?',
        confirmButtonText: 'Ta bort'
      },
      panelClass: 'apk-dialog'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'confirm') {
        this.http.delete('/api/user/' + user.id)
          .subscribe(response => {
            console.log(response);
            this.updateUsers();
            //this.snackBar.open('Lyckades radera!', null, {duration: 3000});
          });
      }
    });
  }

}
