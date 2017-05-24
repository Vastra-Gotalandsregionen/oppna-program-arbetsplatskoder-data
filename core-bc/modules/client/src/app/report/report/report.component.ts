import { Component, OnInit } from '@angular/core';
import {FormControl} from '@angular/forms';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss']
})
export class ReportComponent implements OnInit {

  fromDate = new FormControl();

  constructor() { }

  ngOnInit() {
    let now = new Date();
    now.setMilliseconds(0);
    now.setSeconds(0);
    now.setMinutes(0);
    now.setHours(0);
    now.setDate(1);

    this.fromDate.setValue(now.toLocaleDateString());
  }

}
