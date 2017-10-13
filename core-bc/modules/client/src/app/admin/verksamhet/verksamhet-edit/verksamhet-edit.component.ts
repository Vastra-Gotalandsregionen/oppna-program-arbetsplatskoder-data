import { Component, OnInit } from '@angular/core';
import {Verksamhet} from "../../../model/verksamhet";
import {JwtHttp} from "../../../core/jwt-http";
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {MatSnackBar} from '@angular/material';

@Component({
  selector: 'app-verksamhet-edit',
  templateUrl: './verksamhet-edit.component.html',
  styleUrls: ['./verksamhet-edit.component.scss']
})
export class VerksamhetEditComponent implements OnInit {

  verksamhet: Verksamhet = new Verksamhet();

  verksamhetForm: FormGroup;

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
          // No id means we're creating a new Verksamhet. Just build the form.
          this.buildForm();
          return false; // Stop here.
        }
      }) // Check we have a producentid, otherwise it's a new Prodn3 to be created.
      .mergeMap(params => http.get(`/api/verksamhet/${params.id}`))
      .map(response => response.json())
      .subscribe(verksamhet => {
        this.verksamhet = verksamhet;
        this.buildForm();
      });
  }

  ngOnInit() {
  }

  buildForm() {
    this.verksamhetForm = this.formBuilder.group({
      'id': [{value: this.verksamhet.id, disabled: true}],
      'verksamhetid': [this.verksamhet.verksamhetid, Validators.required],
      'verksamhettext': [this.verksamhet.verksamhettext, Validators.required],
      'raderad': [this.verksamhet.raderad, []]
    });

  }

  save() {

    if (!this.verksamhetForm.valid) {
      this.saveMessage = 'Alla fält är inte korrekt ifyllda.';
      return;
    }

    const formModel = this.verksamhetForm.value;

    const verksamhet = {
      id: this.verksamhet.id,
      verksamhetid: formModel.verksamhetid,
      verksamhettext: formModel.verksamhettext,
      raderad: formModel.raderad ? 'true' : 'false'
    };

    this.http.put('/api/verksamhet', verksamhet)
      .map(response => response.json())
      .subscribe(verksamhet => {
          this.verksamhet = verksamhet;
          this.snackBar.open('Lyckades spara!', null, {duration: 3000});
          this.buildForm();
        }
      );
  }

  resetForm() {
    this.verksamhetForm.reset();
    this.buildForm();
  }


}
