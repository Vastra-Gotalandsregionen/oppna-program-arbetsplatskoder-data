import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {JwtHttp} from '../../../core/jwt-http';
import {Prodn1} from '../../../model/prodn1';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MatSnackBar} from '@angular/material';
import {ErrorHandler} from '../../../shared/error-handler';

@Component({
  selector: 'app-prodn1-edit',
  templateUrl: './prodn1-edit.component.html',
  styleUrls: ['./prodn1-edit.component.scss']
})
export class Prodn1EditComponent implements OnInit {

  prodn1: Prodn1 = new Prodn1();

  prodn1Form: FormGroup;

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
          // No producentid means we're creating a new Prodn1. Just build the form.
          this.buildForm();
          return false;
        }
      }) // Check we have a producentid, otherwise it's a new Prodn1 to be created.
      .mergeMap(params => http.get(`/api/prodn1/${params.id}`))
      .map(response => response.json())
      .subscribe(prodn1 => {
        this.prodn1 = prodn1;
        this.buildForm();
      });
  }

  ngOnInit() {
  }

  buildForm() {
    this.prodn1Form = this.formBuilder.group({
      'id': [{value: this.prodn1.id, disabled: true}],
      'kortnamn': [this.prodn1.kortnamn, Validators.required],
      'raderad': [this.prodn1.raderad, []]
    });
  }

  save() {

    if (!this.prodn1Form.valid) {
      this.saveMessage = 'Alla fält är inte korrekt ifyllda.';
      return;
    }

    const formModel = this.prodn1Form.value;

    const prodn1 = {
      id: this.prodn1.id,
      kortnamn: formModel.kortnamn,
      producentid: this.prodn1.producentid, // Just keep the old value.
      raderad: formModel.raderad ? 'true' : 'false'
    };

    this.http.put('/api/prodn1', prodn1)
      .map(response => response.json())
      .subscribe(prodn1 => {
          this.prodn1 = prodn1;
          this.snackBar.open('Lyckades spara!', null, {duration: 3000});
          this.buildForm();
        }
      );
  }

  resetForm() {
    this.prodn1Form.reset();
    this.buildForm();
  }

}
