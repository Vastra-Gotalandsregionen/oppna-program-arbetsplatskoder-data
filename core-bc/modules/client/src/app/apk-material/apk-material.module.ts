import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  MdAutocompleteModule, MdButtonModule, MdCardModule, MdIconModule, MdInputModule, MdListModule, MdRadioModule,
  MdSidenavModule,
  MdToolbarModule, MdTooltipModule
} from "@angular/material";

@NgModule({
  imports: [
    CommonModule
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
    MdTooltipModule
  ]
})
export class ApkMaterialModule { }
