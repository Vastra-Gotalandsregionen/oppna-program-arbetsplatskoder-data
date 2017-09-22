import { Component, OnInit} from '@angular/core';
import {Data} from "../../model/data";
import {ActivatedRoute} from '@angular/router';
import {JwtHttp} from "../../core/jwt-http";

@Component({
  selector: 'app-archived-datas',
  templateUrl: './archived-datas.component.html',
  styleUrls: ['./archived-datas.component.scss']
})
export class ArchivedDatasComponent implements OnInit {

  // id: string;
  data: Data;
  /*newerData: Data;
  olderData: Data;*/
  archivedDatas: Data[];
  currentPlusArchivedDatas: Data[];

  constructor(protected route: ActivatedRoute,
              protected http: JwtHttp) { }

  ngOnInit() {

    const id = this.route.snapshot.params['id'];

    this.http.get('/api/data/' + id)
      .map(response => response.json())
      .do((data: Data) => {
        this.data = data;
      })
      .mergeMap((data: Data) => this.http.get('/api/archivedData/' + data.id))
      .map(response => response.json())
      .subscribe((archivedDatas: Data[]) => {
        this.currentPlusArchivedDatas = [this.data, ...archivedDatas];
      });

   /* this.route.params.subscribe(params => {
      console.log(params);
      this.id = params.id;

      if (this.id) {
        this.http.get('/api/data/' + this.id)
          .map(response => response.json())
          .subscribe((data: Data) => {
            this.data = data;
          });
      }
    });*/
  }


}
