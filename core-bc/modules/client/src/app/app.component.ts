import { Component } from '@angular/core';
import {StateService} from "./core/state/state.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(public stateService: StateService) {}
}
