import { NgModule } from '@angular/core';
import {
  MdAutocompleteModule, MdButtonModule, MdCardModule, MdCheckboxModule, MdIconModule, MdInputModule, MdListModule,
  MdRadioModule, MdSelectModule,
  MdSidenavModule, MdSnackBarModule,
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
    MdCheckboxModule,
    MdSelectModule,
    MdSnackBarModule
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
    MdCheckboxModule,
    MdSelectModule,
    MdSnackBarModule
  ]
})
export class ApkMaterialModule { }
