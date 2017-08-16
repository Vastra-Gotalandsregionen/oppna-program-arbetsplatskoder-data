import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormGroup} from "@angular/forms";
import {AuthService} from "../../core/auth/auth.service";
import {JwtHttp} from "../../core/jwt-http";
import {Link} from "../../model/link";
import {MdSnackBar} from "@angular/material";

@Component({
  selector: 'app-link-list',
  templateUrl: './link-list.component.html',
  styleUrls: ['./link-list.component.scss']
})
export class LinkListComponent implements OnInit {

  formGroup: FormGroup;

  _links = [];

  editingLinks = [false, false, false];

  constructor(private authService: AuthService,
              private formBuilder: FormBuilder,
              private snackBar: MdSnackBar,
              private http: JwtHttp) {

  }

  ngOnInit() {

    this.http.get('/api/link')
      .map(response => response.json())
      .subscribe((links: Link[]) => {
        this._links = links;
        this.formGroup = this.formBuilder.group({
          'links': this.formBuilder.array(links.map(link => this.formBuilder.group(link)))
        });
      });
  }

  editingLink(index) {
    return this.editingLinks[index];
  }

  editLink(index) {
    this.editingLinks[index] = true;
  }

  saveLink(index) {
    this.editingLinks[index] = false;

    this._links[index].label = this.links.controls[index].get('label').value;

    let url = this.links.controls[index].get('url').value;

    if (url.indexOf('http') !== 0) {
      url = 'http://' + url;
      this.links.controls[index].patchValue({url: url});
    }

    this.http.put('/api/link', this.links.controls[index].value)
      .map(response => response.json())
      .subscribe(link => {
          // this.prodn1 = prodn1;
          this.snackBar.open('Lyckades spara!', null, {duration: 3000});
          this.ngOnInit();
        }, error => {
          console.log(error);
        }
      );
  }

  cancelLink(index) {
    this.links.controls[index].reset({
      label: this._links[index].label,
      url: this._links[index].url
    });

    this.editingLinks[index] = false;
  }

  get links(): FormArray {
    if (this.formGroup && this.formGroup.get('links')) {
      return this.formGroup.get('links') as FormArray;
    } else {
      return null;
    }
  }

  get admin() {
    return this.authService.getLoggedInRole() === 'ADMIN';
  }

}