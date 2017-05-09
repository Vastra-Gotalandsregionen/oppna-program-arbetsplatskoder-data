import { Component, OnInit } from '@angular/core';
import {Prodn2} from '../../model/prodn2';
import {JwtHttp} from '../../core/jwt-http';
import {ErrorHandler} from '../../shared/error-handler';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {MdSnackBar} from '@angular/material';
import {Prodn1} from '../../model/prodn1';
import {Observable} from 'rxjs/Observable';

@Component({
  selector: 'app-prodn2-edit',
  templateUrl: './prodn2-edit.component.html',
  styleUrls: ['./prodn2-edit.component.scss']
})
export class Prodn2EditComponent implements OnInit {

  prodn2: Prodn2 = new Prodn2();

  prodn2Form: FormGroup;

  n1SearchResult$: Observable<Prodn1[]>;

  saveMessage: string;

  constructor(route: ActivatedRoute,
              private formBuilder: FormBuilder,
              private snackBar: MdSnackBar,
              private http: JwtHttp,
              private errorHandler: ErrorHandler) {
    route.params
      .filter(params => {
        if (params.producentid) {
          return true;
        } else {
          // No producentid means we're creating a new Prodn2. Just build the form.
          this.buildForm();
          return false;
        }
      }) // Check we have a producentid, otherwise it's a new Prodn2 to be created.
      .mergeMap(params => http.get(`/api/prodn2/${params.producentid}`))
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
      'avdelning': [this.prodn2.avdelning, Validators.required],
      'kortnamn': [this.prodn2.kortnamn, Validators.required],
      'producentid': [{value: this.prodn2.producentid, disabled: this.prodn2.producentid}, Validators.required],
      'n1': [this.prodn2.n1, Validators.required],
      'riktvarde': [this.prodn2.riktvarde, []],
      'raderad': [this.prodn2.raderad, []]
    });

    this.n1SearchResult$ = this.prodn2Form.get('n1')
      .valueChanges
      .startWith('')
      .mergeMap(value => this.http.get('/api/prodn1/search?query=' + value))
      .map(response => response.json());
  }

  save() {

    if (!this.prodn2Form.valid) {
      this.saveMessage = 'Alla fält är inte korrekt ifyllda.';
      return;
    }

    const formModel = this.prodn2Form.value;

    const prodn2 = {
      id: this.prodn2.id,
      avdelning: formModel.avdelning,
      kortnamn: formModel.kortnamn,
      producentid: formModel.producentid || this.prodn2.producentid, // Either formModel, if new entity, or the old value.
      n1: formModel.n1,
      riktvarde: formModel.riktvarde,
      raderad: formModel.raderad ? 'true' : 'false'
    };

    this.http.put('/api/prodn2', prodn2)
      .map(response => response.json())
      .subscribe(prodn2 => {
          this.prodn2 = prodn2;
          this.snackBar.open('Lyckades spara!', null, {duration: 3000});
          this.buildForm();
        }, error => this.errorHandler.notifyError(error)
      );
  }

  resetForm() {
    this.prodn2Form.reset();
    this.buildForm();
  }
}
