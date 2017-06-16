import { Component, OnInit } from '@angular/core';
import {FormControl} from '@angular/forms';
import {Http, URLSearchParams, RequestOptions} from '@angular/http';

import {Report} from '../../model/report';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss']
})
export class ReportComponent implements OnInit {

  validWithoutEndDateReport: Report = new Report();
  validWithEndDateReport: Report = new Report();
  updatedBetweenDatesReport: Report = new Report();

  fromDate = new FormControl();
  toDate = new FormControl();

  constructor(protected http: Http) {}

  ngOnInit() {
    let now = new Date();
    now.setMilliseconds(0);
    now.setSeconds(0);
    now.setMinutes(0);
    now.setHours(0);
    now.setDate(1);
    now.setMonth(now.getMonth() - 1);

    this.fromDate.setValue(now.toLocaleDateString());

    const lastInMonth = new Date(now.getFullYear(), now.getMonth() + 1, 0).toLocaleDateString();

    this.toDate.setValue(lastInMonth);
  }

  generateValidWithoutEndDateReport(): void {
    this.generateReport('VALID_WITHOUT_END_DATE', this.validWithoutEndDateReport);
  }

  generateValidWithEndDateReport(): void {
    this.generateReport('VALID_WITH_END_DATE', this.validWithEndDateReport);
  }

  generateUpdatedBetweenDatesReport(): void {
    let params: URLSearchParams = new URLSearchParams();
    params.set('fromDate', this.fromDate.value);
    params.set('toDate', this.toDate.value);

    this.generateReport('UPDATED_BETWEEN_DATES', this.updatedBetweenDatesReport, params);
  }

  private generateReport(reportType: string, report: Report, params?: URLSearchParams) {

    report.isLoading = true;

    const requestOptions = new RequestOptions();
    requestOptions.params = params;

    this.http.get('/api/report/generate/' + reportType, requestOptions)
      .map(response => response.json())
      .finally(() => report.isLoading = false)
      .subscribe((report: Report) => {
        this.validWithoutEndDateReport = report;
        window.location.href = `/api/report/file/${reportType}/${report.fileName}/${report.hmac}?${params ? params.toString() : ''}`;
      });
  }

}
