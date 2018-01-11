import {AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {AuthService} from "../../core/auth/auth.service";
import {JwtHttp} from "../../core/jwt-http";
import {StateService} from '../../core/state/state.service';
import {Content} from "../../model/content";
import {MatSnackBar} from "@angular/material";

@Component({
  selector: 'app-content-box',
  templateUrl: './content-box.component.html',
  styleUrls: ['./content-box.component.scss']
})
export class ContentBoxComponent implements OnInit, AfterViewInit {

  @Input('contentId') contentId: string;

  @ViewChild('rootDiv') contentDiv: ElementRef;

  formGroup: FormGroup;

  content: Content;

  editingContent = false;

  stateService: StateService;

  expandable = false;
  expanded = false;
  offsetHeight = 'auto';

  heightLimit = 256;

  constructor(private authService: AuthService,
              private http: JwtHttp,
              private snackBar: MatSnackBar,
              stateService: StateService,
              private formBuilder: FormBuilder) {

      this.stateService = stateService;
  }


  ngOnInit() {

    this.http.get('/api/content/' + this.contentId)
      .map(response => response.json())
      .subscribe(content => {
        this.content = content;

        this.formGroup = this.formBuilder.group({
          'content': this.content.content
        });
        const ref = this.contentDiv;
        setTimeout(() => {
          const contentDiv = ref.nativeElement.getElementsByClassName('apk-content');
          if (contentDiv.length === 1) {
            const offsetHeight = contentDiv[0].offsetHeight;
            this.offsetHeight = offsetHeight;
            if (offsetHeight > this.heightLimit) {
              this.expandable = true;
            }
          }
        }, 0);


      });
  }

  ngAfterViewInit() {
    // debugger;
    // console.log(this.contentDiv.nativeElement.offsetHeight)
  }

  editContent() {
    this.editingContent = true;
    this.formGroup.patchValue({'content': this.content.content});
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
    this.editingContent = false;
  }

  get admin() {
    return this.authService.isAdmin();
  }

  getShowContentEdit(): boolean {
    return this.stateService.showContentEdit;
  }

  setShowContentEdit(value : boolean) {
    this.stateService.showContentEdit = value;
  }

  toggleExpand() {
    this.expanded = !this.expanded;
  }

  get height() {
    return this.expanded ? this.offsetHeight + 'px' : this.heightLimit + 'px';
  }

}
