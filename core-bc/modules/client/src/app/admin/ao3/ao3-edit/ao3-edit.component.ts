import { Component, OnInit } from '@angular/core';
import {Ao3} from "../../../model/ao3";
import {JwtHttp} from "../../../core/jwt-http";
import {Response} from '@angular/http';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {MdSnackBar} from '@angular/material';
import {Prodn1} from "../../../model/prodn1";
import {Location} from "@angular/common";

@Component({
  selector: 'app-ao3-edit',
  templateUrl: './ao3-edit.component.html',
  styleUrls: ['./ao3-edit.component.scss']
})
export class Ao3EditComponent implements OnInit {
  selectedProdn1s: Prodn1[] = [];

  allProdn1s: Prodn1[];

  ao3: Ao3 = new Ao3();

  ao3Form: FormGroup;

  saveMessage: string;

  editProdn1 = false;

  selectedProdn1Ids: number[] = [];

  prodn1sMap: Map<number, Prodn1> = new Map();

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
      .subscribe((ao3: Ao3) => {
        this.ao3 = ao3;
        this.buildForm();
        this.selectedProdn1Ids = ao3.prodn1s.map(prodn1 => prodn1.id);
      });
  }

  ngOnInit() {
    this.http.get('/api/prodn1')
      .map<Response, Prodn1[]>(response => response.json())
      .subscribe((prodn1s: Prodn1[]) => {
        this.allProdn1s = prodn1s;
        prodn1s.forEach(prodn1 => this.prodn1sMap.set(prodn1.id, prodn1));
        this.selectedProdn1s = this.selectedProdn1Ids.map(id => this.prodn1sMap.get(id));
      });

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
      prodn1s: this.selectedProdn1s,
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
          this.editProdn1 = false;
        }
      );
  }

  toggleProdn1(prodn1: Prodn1) {
    const indexOf = this.selectedProdn1Ids.indexOf(prodn1.id);
    if (indexOf > -1) {
      this.selectedProdn1Ids.splice(indexOf, 1);
    } else {
      this.selectedProdn1Ids.push(prodn1.id);
    }

    this.selectedProdn1s = this.selectedProdn1Ids.map(id => this.prodn1sMap.get(id));

    this.ao3Form.markAsDirty();
    this.ao3Form.markAsTouched()
  }

  isSelected(prodn1: Prodn1) {
    return this.selectedProdn1Ids.indexOf(prodn1.id) > -1;
  }

  toggleEditProdn1() {
    this.editProdn1 = !this.editProdn1;
  }

  resetForm() {
    this.ao3Form.reset();
    this.buildForm();
  }


}
