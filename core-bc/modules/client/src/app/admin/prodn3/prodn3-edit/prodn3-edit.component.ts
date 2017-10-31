import { Component, OnInit } from '@angular/core';
import {Prodn3} from '../../../model/prodn3';
import {Prodn2} from '../../../model/prodn2';
import {JwtHttp} from '../../../core/jwt-http';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {MatSnackBar} from '@angular/material';
import {Prodn1} from "../../../model/prodn1";

@Component({
  selector: 'app-prodn3-edit',
  templateUrl: './prodn3-edit.component.html',
  styleUrls: ['./prodn3-edit.component.scss']
})
export class Prodn3EditComponent implements OnInit {

  prodn3: Prodn3 = new Prodn3();

  prodn3Form: FormGroup;

  prodn1Options: Prodn1[] = [];
  prodn2Options: Prodn2[] = [];

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
          // No producentid means we're creating a new Prodn3. Just build the form.
          this.buildForm();
          return false;
        }
      }) // Check we have a producentid, otherwise it's a new Prodn3 to be created.
      .mergeMap(params => http.get(`/api/prodn3/${params.id}`))
      .map(response => response.json())
      .subscribe(prodn3 => {
        this.prodn3 = prodn3;
        this.buildForm();
      });
  }

  ngOnInit() {

    this.http.get('/api/prodn1').map(response => response.json())
      .subscribe(prodn1Options => this.prodn1Options = prodn1Options);

  }

  buildForm() {
    this.prodn3Form = this.formBuilder.group({
      'id': [{value: this.prodn3.id, disabled: true}],
      'kortnamn': [this.prodn3.kortnamn, Validators.required],
      'prodn1': [this.prodn3.prodn2 && this.prodn3.prodn2.prodn1 ? this.prodn3.prodn2.prodn1.id : null, Validators.required],
      'prodn2': [this.prodn3.prodn2 ? this.prodn3.prodn2.id : null, Validators.required],
      'raderad': [this.prodn3.raderad, []]
    });

    this.prodn3Form.get('prodn1').valueChanges
      .startWith(this.prodn3.prodn2 ? this.prodn3.prodn2.prodn1.id : null)
      .filter(value => value) // This solution with startWith + filter makes so we make a query when we have a value as well as not query for prodn2s when we don't have a prodn1.
      .mergeMap(prodn1Id => this.http.get('/api/prodn2?prodn1=' + prodn1Id))
      .map(response => response.json().content)
      .subscribe(prodn2s => this.prodn2Options = prodn2s);
  }

  save() {

    if (!this.prodn3Form.valid) {
      this.saveMessage = 'Alla fält är inte korrekt ifyllda.';
      return;
    }

    const formModel = this.prodn3Form.value;

    const prodn3 = {
      id: this.prodn3.id,
      kortnamn: formModel.kortnamn,
      prodn2: this.getProdn2ById(formModel.prodn2),
      producentid: this.prodn3.producentid, // Just keep the old value.
      raderad: formModel.raderad ? 'true' : 'false',
      autoradering: this.prodn3.autoradering // Just keep the old value.
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

  private getProdn2ById(id) {
    let filtered = this.prodn2Options.filter(prodn2 => prodn2.id === id);

    return filtered[0];
  }

  resetForm() {
    this.prodn3Form.reset();
    this.buildForm();
  }

}
