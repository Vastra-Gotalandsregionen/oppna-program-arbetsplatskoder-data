import { NgModule } from '@angular/core';
import {
  MdAutocompleteModule, MdButtonModule, MdCardModule, MdCheckboxModule, MdDialogModule, MdIconModule, MdInputModule,
  MdListModule,
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
    MdSnackBarModule,
    MdDialogModule
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
    MdSnackBarModule,
    MdDialogModule
  ]
})
export class ApkMaterialModule { }
