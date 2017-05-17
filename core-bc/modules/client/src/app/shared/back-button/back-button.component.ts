import { Component, OnInit, Input } from '@angular/core';
import {Location} from "@angular/common";


@Component({
  selector: 'app-back-button',
  templateUrl: './back-button.component.html',
  styleUrls: ['./back-button.component.scss']
})
export class BackButtonComponent implements OnInit {

  @Input()
  tooltip: String = 'Gå tillbaka';

  @Input()
  icon: String = 'arrow_back';

  constructor(private location: Location) { }

  ngOnInit() {
  }

  backClicked() {
  	this.location.back();
  }

}
