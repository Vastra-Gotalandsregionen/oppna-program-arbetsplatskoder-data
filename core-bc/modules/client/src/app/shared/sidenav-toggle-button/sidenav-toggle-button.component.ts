import { Component, OnInit, Input } from '@angular/core';
import {Location} from "@angular/common";

import {StateService} from '../../core/state/state.service';


@Component({
  selector: 'app-sidenav-toggle-button',
  templateUrl: './sidenav-toggle-button.component.html',
  styleUrls: ['./sidenav-toggle-button.component.scss']
})
export class SidenavToggleButtonComponent implements OnInit {

  @Input()
  tooltip: String = 'Visa / dölj sidomenyn';

  @Input()
  icon: String = 'dehaze';

  constructor(private stateService: StateService) { }

  ngOnInit() {
  }

  toggleClicked() {
  	this.stateService.toggleSidenav();
  }

}
