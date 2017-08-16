import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {AuthService} from "../../core/auth/auth.service";
import {JwtHttp} from "../../core/jwt-http";
import {Content} from "../../model/content";
import {MdSnackBar} from "@angular/material";

@Component({
  selector: 'app-content-box',
  templateUrl: './content-box.component.html',
  styleUrls: ['./content-box.component.scss']
})
export class ContentBoxComponent implements OnInit {

  @Input('contentId') contentId: string;

  formGroup: FormGroup;

  content: Content;

  editingContent = false;

  constructor(private authService: AuthService,
              private http: JwtHttp,
              private snackBar: MdSnackBar,
              private formBuilder: FormBuilder) {
  }

  ngOnInit() {

    this.http.get('/api/content/' + this.contentId)
      .map(response => response.json())
      .subscribe(content => {
        this.content = content;

        this.formGroup = this.formBuilder.group({
          'content': this.content.content
        });

      });
  }

  editContent() {
    this.editingContent = true;
  }

  saveContent() {
    this.editingContent = false;
    this.content.content = this.formGroup.get('content').value;

    this.http.put('/api/content', this.content)
      .map(response => response.json())
      .subscribe(content => {
        this.snackBar.open('Lyckades spara!', null, {duration: 3000});

      }, error => {
        console.log(error);
      });
  }

  cancelContent() {
    this.formGroup.patchValue({'content': this.content});
    this.editingContent = false;
  }

  get admin() {
    return this.authService.getLoggedInRole() === 'ADMIN';
  }

}
