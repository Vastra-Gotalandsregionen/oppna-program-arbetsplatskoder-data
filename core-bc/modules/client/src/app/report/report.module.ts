import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {SharedModule} from "../shared/shared.module";
import { ReportComponent } from './report/report.component';
import {ReportRoutingModule} from "./report-routing.module";

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    ReportRoutingModule
  ],
  declarations: [ReportComponent]
})
export class ReportModule { }
