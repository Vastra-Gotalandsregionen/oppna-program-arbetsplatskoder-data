import { Component, OnInit } from '@angular/core';
import {Prodn2} from '../../../model/prodn2';
import {JwtHttp} from '../../../core/jwt-http';
import {ErrorHandler} from '../../../shared/error-handler';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {MatSnackBar} from '@angular/material';
import {Prodn1} from '../../../model/prodn1';
import {Observable} from 'rxjs/Observable';

@Component({
  selector: 'app-prodn2-edit',
  templateUrl: './prodn2-edit.component.html',
  styleUrls: ['./prodn2-edit.component.scss']
})
export class Prodn2EditComponent implements OnInit {

  prodn2: Prodn2 = new Prodn2();

  prodn2Form: FormGroup;

  prodn1Options: Prodn1[];

  saveMessage: string;

  constructor(route: ActivatedRoute,
              private formBuilder: FormBuilder,
              private snackBar: MatSnackBar,
              private http: JwtHttp,
              private errorHandler: ErrorHandler) {
    route.params
      .filter(params => {
        if (params.id) {
          return true;
        } else {
          // No producentid means we're creating a new Prodn2. Just build the form.
          this.buildForm();
          return false;
        }
      }) // Check we have a producentid, otherwise it's a new Prodn2 to be created.
      .mergeMap(params => http.get(`/api/prodn2/${params.id}`))
      .map(response => response.json())
      .subscribe(prodn2 => {
        this.prodn2 = prodn2;
        this.buildForm();
      });
  }

  ngOnInit() {
  }

  buildForm() {
    this.prodn2Form = this.formBuilder.group({
      'id': [{value: this.prodn2.id, disabled: true}],
      'kortnamn': [this.prodn2.kortnamn, Validators.required],
      'prodn1': [this.prodn2.prodn1 ? this.prodn2.prodn1.id : null, Validators.required],
      'raderad': [this.prodn2.raderad, []]
    });

    this.http.get('/api/prodn1').map(response => response.json())
      .subscribe(prodn1Options => this.prodn1Options = prodn1Options);

   /* this.n1SearchResult$ = this.prodn2Form.get('n1')
      .valueChanges
      .startWith('')
      .mergeMap(value => this.http.get('/api/prodn1/search?query=' + value))
      .map(response => response.json());*/
  }

  save() {

    if (!this.prodn2Form.valid) {
      this.saveMessage = 'Alla fält är inte korrekt ifyllda.';
      return;
    }

    const formModel = this.prodn2Form.value;

    const prodn2 = {
      id: this.prodn2.id,
      kortnamn: formModel.kortnamn,
      producentid: this.prodn2.producentid, // Just keep the old value.
      n1: this.prodn2.n1, // Just keep the old value.
      prodn1: this.getProdn1ById(formModel.prodn1),
      raderad: formModel.raderad ? 'true' : 'false',
      autoradering: this.prodn2.autoradering // Just keep the old value.
    };

    this.http.put('/api/prodn2', prodn2)
      .map(response => response.json())
      .subscribe(prodn2 => {
          this.prodn2 = prodn2;
          this.snackBar.open('Lyckades spara!', null, {duration: 3000});
          this.buildForm();
        }
      );
  }

  private getProdn1ById(id) {
    let filtered = this.prodn1Options.filter(prodn1 => prodn1.id === id);

    return filtered[0];
  }

  resetForm() {
    this.prodn2Form.reset();
    this.buildForm();
  }
}
