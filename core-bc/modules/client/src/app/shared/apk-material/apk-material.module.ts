import { NgModule } from '@angular/core';
import {
  MdAutocompleteModule, MdButtonModule, MdButtonToggleModule,
  MdCardModule, MdCheckboxModule, MdChipsModule, MdDialogModule, MdIconModule,
  MdInputModule,
  MdListModule, MdProgressBarModule,
  MdRadioModule, MdSelectModule,
  MdSidenavModule, MdSnackBarModule,
  MdToolbarModule, MdTooltipModule
} from '@angular/material';

@NgModule({
  imports: [
    MdAutocompleteModule,
    MdIconModule,
    MdInputModule,
    MdSidenavModule,
    MdToolbarModule,
    MdListModule,
    MdButtonModule,
    MdButtonToggleModule,
    MdCardModule,
    MdRadioModule,
    MdTooltipModule,
    MdCheckboxModule,
    MdSelectModule,
    MdSnackBarModule,
    MdDialogModule,
    MdProgressBarModule,
    MdChipsModule
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
    MdButtonToggleModule,
    MdCardModule,
    MdRadioModule,
    MdTooltipModule,
    MdCheckboxModule,
    MdSelectModule,
    MdSnackBarModule,
    MdDialogModule,
    MdProgressBarModule,
    MdChipsModule
  ]
})
export class ApkMaterialModule { }
