import { Component, OnInit } from '@angular/core';
import {Ao3} from "../../../model/ao3";
import {JwtHttp} from "../../../core/jwt-http";
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {MdSnackBar} from '@angular/material';
import {Prodn1} from "../../../model/prodn1";
import {Observable} from "rxjs/Observable";
import {Location} from "@angular/common";

@Component({
  selector: 'app-ao3-edit',
  templateUrl: './ao3-edit.component.html',
  styleUrls: ['./ao3-edit.component.scss']
})
export class Ao3EditComponent implements OnInit {

  ao3: Ao3 = new Ao3();

  ao3Form: FormGroup;

  prodn1SearchResult$: Observable<Prodn1[]>;

  saveMessage: string;

  constructor(route: ActivatedRoute,
              private formBuilder: FormBuilder,
              private snackBar: MdSnackBar,
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
      .subscribe(ao3 => {
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
      'producent': [this.ao3.producent, Validators.required],
      'foretagsnr': [this.ao3.foretagsnr, Validators.required],
      'kontaktperson': [this.ao3.kontaktperson, Validators.required],
      'raderad': [this.ao3.raderad, []]
    });

    const prodn1Input$ = this.ao3Form.get('producent').valueChanges.startWith('');

    this.prodn1SearchResult$ = this.http.get('/api/prodn1').map(response => response.json())
      .combineLatest(prodn1Input$, (prodn1s: Prodn1[], query) => {
        const filtered = prodn1s.filter(prodn1 => prodn1.foretagsnamn.indexOf(query) > -1 || prodn1.producentid.indexOf(query) > -1);
        return filtered;
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
      producent: formModel.producent,
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
