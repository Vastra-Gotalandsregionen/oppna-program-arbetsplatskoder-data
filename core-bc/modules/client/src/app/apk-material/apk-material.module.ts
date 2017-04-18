import { NgModule } from '@angular/core';
import {
  MdAutocompleteModule, MdButtonModule, MdCardModule, MdCheckboxModule, MdIconModule, MdInputModule, MdListModule,
  MdRadioModule,
  MdSidenavModule,
  MdToolbarModule, MdTooltipModule
} from "@angular/material";

@NgModule({
  imports: [
    MdAutocompleteModule,
    MdIconModule,
    MdInputModule,
    MdSidenavModule,
    MdToolbarModule,
    MdListModule,
    MdButtonModule,
    MdCardModule,
    MdRadioModule,
    MdTooltipModule,
    MdCheckboxModule
  ],
  declarations: [],
  exports: [
    MdAutocompleteModule,
    MdIconModule,
    MdInputModule,
    MdSidenavModule,
    MdToolbarModule,
    MdListModule,
    MdButtonModule,
    MdCardModule,
    MdRadioModule,
    MdTooltipModule,
    MdCheckboxModule
  ]
})
export class ApkMaterialModule { }
