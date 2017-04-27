import {Component, OnInit} from "@angular/core";
import {Http, Response} from "@angular/http";
import {ErrorHandler} from "../../shared/error-handler";
import {User} from "../../model/user";

@Component({
  selector: 'app-users-list',
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.scss']
})
export class UsersListComponent implements OnInit {

  users: User[];

  constructor(private http: Http,
              private errorHandler: ErrorHandler) { }

  ngOnInit() {
    this.http.get('/api/user').map<Response, User[]>(response => response.json())
      .subscribe(
        users => this.users = users,
        error => this.errorHandler.notifyError(error)
      );
  }

}
