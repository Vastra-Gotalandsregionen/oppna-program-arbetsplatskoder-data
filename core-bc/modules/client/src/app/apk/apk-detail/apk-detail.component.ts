import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Data} from '../../model/data';
import {AuthService} from '../../core/auth/auth.service';
import {ApkBase} from "../apk-base/apk-base";
import {Observable} from 'rxjs/Observable';
import {ApkFormComponent} from "../apk-form/apk-form.component";
import {JwtHttp} from "../../core/jwt-http";

@Component({
  selector: 'app-apk-detail',
  templateUrl: './apk-detail.component.html',
  styleUrls: ['./apk-detail.component.css']
})
export class ApkDetailComponent extends ApkBase implements OnInit {

  @ViewChild(ApkFormComponent) apkFormComponent: ApkFormComponent;

  id: string;
  data: Data;
  $replacedBy: Observable<Data>;
  $replaces: Observable<Data>;
  archivedDatas: Data[] = [];

  constructor(protected route: ActivatedRoute,
              protected http: JwtHttp,
              protected authService: AuthService) {
    super();
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.id = params.id;

      if (this.id) {
        const $data = this.http.get('/api/data/' + this.id)
          .map(response => response.json())
          .share();

        $data.subscribe((data: Data) => {
          this.data = data;
        });

        this.$replacedBy = $data
          .filter((data: Data) => !!data.ersattav)
          .mergeMap((data: Data) => this.http.get('/api/data/arbetsplatskodlan/' + data.ersattav))
          .map(response => response.json());

        this.$replaces = $data
          .mergeMap((data: Data) => this.http.get('/api/data/ersattav/' + data.arbetsplatskodlan))
          .map(response => response.json());

        this.http.getPage('/api/archivedData/' + this.id)
          .map(response => response.json())
          .subscribe((archivedDatas: Data[]) => {
            this.archivedDatas = archivedDatas;
          });

      }
    });
  }

  protected getId() {
    return this.id;
  }

  userHasEditPermission(data: Data) {
    return this.authService.userHasDataEditPermission(data);
  }
}
