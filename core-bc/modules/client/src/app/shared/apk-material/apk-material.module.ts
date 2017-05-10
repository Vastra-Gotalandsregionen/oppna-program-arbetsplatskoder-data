import { NgModule } from '@angular/core';
import {
  MdAutocompleteModule, MdButtonModule, MdButtonToggleModule,
  MdCardModule, MdCheckboxModule, MdChipsModule, MdDialogModule, MdIconModule,
  MdInputModule,
  MdMenuModule,
  MdListModule, MdProgressBarModule,
  MdRadioModule, MdSelectModule,
  MdSlideToggleModule,
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
    MdMenuModule,
    MdButtonModule,
    MdButtonToggleModule,
    MdCardModule,
    MdRadioModule,
    MdTooltipModule,
    MdCheckboxModule,
    MdSelectModule,
    MdSlideToggleModule,
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
    MdMenuModule,
    MdButtonModule,
    MdButtonToggleModule,
    MdCardModule,
    MdRadioModule,
    MdTooltipModule,
    MdCheckboxModule,
    MdSelectModule,
    MdSlideToggleModule,
    MdSnackBarModule,
    MdDialogModule,
    MdProgressBarModule,
    MdChipsModule
  ]
})
export class ApkMaterialModule { }
