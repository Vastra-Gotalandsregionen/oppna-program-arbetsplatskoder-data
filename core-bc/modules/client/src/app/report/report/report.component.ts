import { Component, OnInit } from '@angular/core';
import {FormControl} from '@angular/forms';
import {Http} from '@angular/http';

import {Report} from '../../model/report';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss']
})
export class ReportComponent implements OnInit {

  allValidReport: Report;

  fromDate = new FormControl();



  constructor(protected http: Http) {
    this.allValidReport = new Report();
    this.allValidReport.isLoading = false;
  }

  ngOnInit() {
    let now = new Date();
    now.setMilliseconds(0);
    now.setSeconds(0);
    now.setMinutes(0);
    now.setHours(0);
    now.setDate(1);

    this.fromDate.setValue(now.toLocaleDateString());
  }

  generateAllValidReport(): void {

    console.log('generateAllValidReport');
    this.allValidReport.isLoading = true;

    this.http.get('/api/report/generate/all_valid')
      .map(response => response.json())
      .subscribe((allValidReport: Report) => {
        this.allValidReport = allValidReport;
        this.allValidReport.isLoading = false;
      });
  }

}
