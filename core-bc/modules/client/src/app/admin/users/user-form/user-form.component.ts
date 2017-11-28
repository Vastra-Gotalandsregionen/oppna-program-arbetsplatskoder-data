import {Component, Input, OnInit} from '@angular/core';
import {ErrorHandler} from '../../../shared/error-handler';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Response} from '@angular/http';
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

  saveMessage: string;

  @Input('userId') userId;

  userForm: FormGroup;

  user: User;
  allProdn1s: Prodn1[];
  prodn1sMap: Map<number, Prodn1>;
  selectedProdn1Ids: number[];

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
        .subscribe((result: any[]) => {
          this.allProdn1s = result[0];
          this.allProdn1s.forEach(prodn1 => this.prodn1sMap.set(prodn1.id, prodn1));
          this.user = result[1];
          this.selectedProdn1Ids = this.user.prodn1s.map(prodn1 => prodn1.id);
          this.buildForm();
        });
    } else {
      this.user = new User();
      this.user.role = 'USER';
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

    let userIdField = this.userForm.get('userId');

    userIdField.valueChanges
      .subscribe(value => {
        if (value) {
          let trimmed = value.trim();
          if (trimmed !== value) {
            userIdField.setValue(trimmed);
          }
        }
      });

    userIdField.setAsyncValidators(this.validateUserId.bind(this))
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
      this.saveMessage = 'Alla fält är inte korrekt ifyllda.';
      return;
    }

    this.user.id = this.userForm.get('userId').value;
    this.user.prodn1s = this.selectedProdn1Ids.map(id => this.prodn1sMap.get(id));
    this.user.role = this.userForm.get('roleGroup').get('role').value;
    this.user.inactivated = this.user.inactivated;

    this.http.put('/api/user', this.user)
      .subscribe(result => {
        this.router.navigate(['/admin/users']);
      });
  }

  // Convenience method for less code in html file.
  getErrors(formControlName: string): any {
    return this.userForm.controls[formControlName].errors;
  }

  validateUserId(control: AbstractControl): Observable<{ [key: string]: any }> {
    let value = control.value;
    const newUser = this.user.id !== value;

    if (value && newUser) {
      return this.http.get('/api/user/' + value)
        .map(response => {
          try {
            return response.json();
          } catch (e) {
            return null;
          }
        })
        .map(user => {
          if (user) {
            control.markAsTouched();
            return {alreadyExists: control.value};
          } else {
            return null;
          }
        });
    } else {
      return new Observable(observer => observer.next(null)).take(1);
    }
  };

}
