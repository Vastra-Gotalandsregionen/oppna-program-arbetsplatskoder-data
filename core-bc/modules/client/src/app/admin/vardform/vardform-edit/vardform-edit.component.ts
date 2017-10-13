import { Component, OnInit } from '@angular/core';
import {Vardform} from "../../../model/vardform";
import {JwtHttp} from "../../../core/jwt-http";
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {MatSnackBar} from '@angular/material';

@Component({
  selector: 'app-vardform-edit',
  templateUrl: './vardform-edit.component.html',
  styleUrls: ['./vardform-edit.component.scss']
})
export class VardformEditComponent implements OnInit {

  vardform: Vardform = new Vardform();

  vardformForm: FormGroup;

  saveMessage: string;

  constructor(route: ActivatedRoute,
              private formBuilder: FormBuilder,
              private snackBar: MatSnackBar,
              private http: JwtHttp) {
    route.params
      .filter(params => {
        if (params.id) {
          return true;
        } else {
          // No id means we're creating a new Vardform. Just build the form.
          this.buildForm();
          return false; // Stop here.
        }
      }) // Check we have a producentid, otherwise it's a new Prodn3 to be created.
      .mergeMap(params => http.get(`/api/vardform/${params.id}`))
      .map(response => response.json())
      .subscribe(vardform => {
        this.vardform = vardform;
        this.buildForm();
      });
  }

  ngOnInit() {
  }

  buildForm() {
    this.vardformForm = this.formBuilder.group({
      'id': [{value: this.vardform.id, disabled: true}],
      'vardformid': [this.vardform.vardformid, Validators.required],
      'vardformtext': [this.vardform.vardformtext, Validators.required],
      'raderad': [this.vardform.raderad, []]
    });

  }

  save() {

    if (!this.vardformForm.valid) {
      this.saveMessage = 'Alla fält är inte korrekt ifyllda.';
      return;
    }

    const formModel = this.vardformForm.value;

    const vardform = {
      id: this.vardform.id,
      vardformid: formModel.vardformid,
      vardformtext: formModel.vardformtext,
      raderad: formModel.raderad ? 'true' : 'false'
    };

    this.http.put('/api/vardform', vardform)
      .map(response => response.json())
      .subscribe(vardform => {
          this.vardform = vardform;
          this.snackBar.open('Lyckades spara!', null, {duration: 3000});
          this.buildForm();
        }
      );
  }

  resetForm() {
    this.vardformForm.reset();
    this.buildForm();
  }


}
