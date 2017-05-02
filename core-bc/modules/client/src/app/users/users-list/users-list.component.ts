import {Component, OnInit} from "@angular/core";
import {Http, Response} from "@angular/http";
import {ErrorHandler} from "../../shared/error-handler";
import {User} from "../../model/user";
import {Prodn1} from "../../model/prodn1";

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
    this.updateUsers();
  }

  private updateUsers() {
    this.http.get('/api/user').map<Response, User[]>(response => response.json())
      .subscribe(
        users => this.users = users,
        error => this.errorHandler.notifyError(error)
      );
  }

  confirmDeleteTODO(user: User) {
    this.http.delete('/api/user/' + user.id)
      .subscribe(response => {
        console.log(response);
        this.updateUsers();
      });
  }

  prodn1sToString(prodn1: Prodn1[]) {
    return prodn1.map(prodn1 => prodn1.producentid).join(', ')
  }

}
