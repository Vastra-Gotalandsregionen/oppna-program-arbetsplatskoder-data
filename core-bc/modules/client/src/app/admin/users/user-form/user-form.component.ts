import {Component, Input, OnInit} from '@angular/core';
import {ErrorHandler} from '../../../shared/error-handler';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Http, Response} from '@angular/http';
import {User} from '../../../model/user';
import {Prodn1} from '../../../model/prodn1';
import {Observable} from 'rxjs/Observable';
import {JwtHttp} from '../../../core/jwt-http';
import {Router} from '@angular/router';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.scss']
})
export class UserFormComponent implements OnInit {

  @Input('userId') userId;

  userForm: FormGroup;

  user: User;
  allProdn1s: Prodn1[];
  prodn1sMap: Map<string, Prodn1>;
  selectedProdn1Ids: string[];

  constructor(private http: JwtHttp,
              private formBuilder: FormBuilder,
              private errorHandler: ErrorHandler,
              private router: Router) { }

  ngOnInit() {

    const prodn1s$ = this.http.get('/api/prodn1')
      .map<Response, Prodn1[]>(response => response.json());

    if (this.userId) {
      const user$ = this.http.get('/api/user/' + this.userId)
        .map<Response, User>(response => response.json());

      Observable.forkJoin([prodn1s$, user$])
        .subscribe(result => {
          this.allProdn1s = result[0];
          this.allProdn1s.forEach(prodn1 => this.prodn1sMap.set(prodn1.id, prodn1));
          this.user = result[1];
          this.selectedProdn1Ids = this.user.prodn1s.map(prodn1 => prodn1.id);
          this.buildForm();
        });
    } else {
      this.user = new User();
      prodn1s$.subscribe(allProdn1 => {
        this.allProdn1s = allProdn1;
        this.allProdn1s.forEach(prodn1 => this.prodn1sMap.set(prodn1.id, prodn1));
        this.selectedProdn1Ids = [];
        this.buildForm();
      });
    }

    this.prodn1sMap = new Map();
  }

  private buildForm() {

    this.userForm = this.formBuilder.group({
      'userId': [{value: this.user.id, disabled: false}, [Validators.required]],
      'roleGroup': this.formBuilder.group({
        'role': [{value: this.user.role, disabled: false}, [Validators.required]]
      })
    });

  }

  toggle(prodn1: Prodn1) {
    const indexOf = this.selectedProdn1Ids.indexOf(prodn1.id);
    if (indexOf > -1) {
      this.selectedProdn1Ids.splice(indexOf, 1);
    } else {
      this.selectedProdn1Ids.push(prodn1.id);
    }
  }

  isSelected(prodn1: Prodn1) {
    return this.selectedProdn1Ids.indexOf(prodn1.id) > -1;
  }

  save() {
    if (!this.userForm.valid) {
      // todo Display some message to user.
      return;
    }

    this.user.id = this.userForm.get('userId').value;
    this.user.prodn1s = this.selectedProdn1Ids.map(id => this.prodn1sMap.get(id));
    this.user.role = this.userForm.get('roleGroup').get('role').value;

    this.http.put('/api/user', this.user)
      .subscribe(result => {
        this.router.navigate(['/admin/users']);
      });
  }
}
