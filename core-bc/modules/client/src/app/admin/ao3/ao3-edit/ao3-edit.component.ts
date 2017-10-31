import { Component, OnInit } from '@angular/core';
import {Ao3} from "../../../model/ao3";
import {JwtHttp} from "../../../core/jwt-http";
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {MatSnackBar} from '@angular/material';
import {Location} from "@angular/common";

@Component({
  selector: 'app-ao3-edit',
  templateUrl: './ao3-edit.component.html',
  styleUrls: ['./ao3-edit.component.scss']
})
export class Ao3EditComponent implements OnInit {
  ao3: Ao3 = new Ao3();

  ao3Form: FormGroup;

  saveMessage: string;

  constructor(route: ActivatedRoute,
              private formBuilder: FormBuilder,
              private snackBar: MatSnackBar,
              private http: JwtHttp,
              public location: Location) {

    route.params
      .filter(params => {
        if (params.id) {
          return true;
        } else {
          // No id means we're creating a new Ao3. Just build the form.
          this.buildForm();
          return false; // Stop here.
        }
      }) // Check we have a producentid, otherwise it's a new Prodn3 to be created.
      .mergeMap(params => http.get(`/api/ao3/${params.id}`))
      .map(response => response.json())
      .subscribe((ao3: Ao3) => {
        this.ao3 = ao3;
        this.buildForm();
      });
  }

  ngOnInit() {

  }

  buildForm() {
    this.ao3Form = this.formBuilder.group({
      'id': [{value: this.ao3.id, disabled: true}],
      'foretagsnamn': [this.ao3.foretagsnamn, Validators.required],
      'ao3id': [this.ao3.ao3id, Validators.required],
      'foretagsnr': [this.ao3.foretagsnr, Validators.required],
      'kontaktperson': [this.ao3.kontaktperson, Validators.required],
      'raderad': [this.ao3.raderad, []]
    });
  }

  save() {

    if (!this.ao3Form.valid) {
      this.saveMessage = 'Alla fält är inte korrekt ifyllda.';
      return;
    }

    const formModel = this.ao3Form.value;

    const ao3 = {
      id: this.ao3.id,
      foretagsnamn: formModel.foretagsnamn,
      ao3id: formModel.ao3id,
      foretagsnr: formModel.foretagsnr,
      kontaktperson: formModel.kontaktperson,
      raderad: formModel.raderad ? 'true' : 'false'
    };

    this.http.put('/api/ao3', ao3)
      .map(response => response.json())
      .subscribe(ao3 => {
          this.ao3 = ao3;
          this.snackBar.open('Lyckades spara!', null, {duration: 3000});
          this.buildForm();
        }
      );
  }

  resetForm() {
    this.ao3Form.reset();
    this.buildForm();
  }


}
