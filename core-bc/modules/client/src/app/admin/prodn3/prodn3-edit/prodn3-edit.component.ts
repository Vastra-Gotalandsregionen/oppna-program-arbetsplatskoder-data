import { Component, OnInit } from '@angular/core';
import {Prodn3} from '../../../model/prodn3';
import {Prodn2} from '../../../model/prodn2';
import {JwtHttp} from '../../../core/jwt-http';
import {ErrorHandler} from '../../../shared/error-handler';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {MdSnackBar} from '@angular/material';
import {Observable} from 'rxjs/Observable';

@Component({
  selector: 'app-prodn3-edit',
  templateUrl: './prodn3-edit.component.html',
  styleUrls: ['./prodn3-edit.component.scss']
})
export class Prodn3EditComponent implements OnInit {

  prodn3: Prodn3 = new Prodn3();

  prodn3Form: FormGroup;

  n2SearchResult$: Observable<Prodn2[]>;

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
          // No producentid means we're creating a new Prodn3. Just build the form.
          this.buildForm();
          return false;
        }
      }) // Check we have a producentid, otherwise it's a new Prodn3 to be created.
      .mergeMap(params => http.get(`/api/prodn3/${params.producentid}`))
      .map(response => response.json())
      .subscribe(prodn3 => {
        this.prodn3 = prodn3;
        this.buildForm();
      });
  }

  ngOnInit() {
  }

  buildForm() {
    this.prodn3Form = this.formBuilder.group({
      'id': [{value: this.prodn3.id, disabled: true}],
      'foretagsnamn': [this.prodn3.foretagsnamn, Validators.required],
      'kortnamn': [this.prodn3.kortnamn, Validators.required],
      'producentid': [{value: this.prodn3.producentid, disabled: this.prodn3.producentid}, Validators.required],
      'n2': [this.prodn3.n2, Validators.required],
      'raderad': [this.prodn3.raderad, []]
    });

    this.n2SearchResult$ = this.prodn3Form.get('n2')
      .valueChanges
      .startWith('')
      .mergeMap(value => this.http.get('/api/prodn2/search?query=' + value))
      .map(response => response.json());
  }

  save() {

    if (!this.prodn3Form.valid) {
      this.saveMessage = 'Alla fält är inte korrekt ifyllda.';
      return;
    }

    const formModel = this.prodn3Form.value;

    const prodn3 = {
      id: this.prodn3.id,
      foretagsnamn: formModel.foretagsnamn,
      kortnamn: formModel.kortnamn,
      producentid: formModel.producentid || this.prodn3.producentid, // Either formModel, if new entity, or the old value.
      n2: formModel.n2,
      raderad: formModel.raderad ? 'true' : 'false'
    };

    this.http.put('/api/prodn3', prodn3)
      .map(response => response.json())
      .subscribe(prodn3 => {
          this.prodn3 = prodn3;
          this.snackBar.open('Lyckades spara!', null, {duration: 3000});
          this.buildForm();
        }
      );
  }

  resetForm() {
    this.prodn3Form.reset();
    this.buildForm();
  }

}
