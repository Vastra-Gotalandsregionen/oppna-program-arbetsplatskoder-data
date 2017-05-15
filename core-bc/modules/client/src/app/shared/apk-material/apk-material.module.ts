import { NgModule } from '@angular/core';
import {MyDateAdapter} from "./my-date-adapter";
import {
  MdAutocompleteModule, MdButtonModule, MdButtonToggleModule,
  MdCardModule, MdCheckboxModule, MdChipsModule, MdDialogModule, MdIconModule,
  MdInputModule,
  MdMenuModule,
  MdListModule, MdProgressBarModule,
  MdRadioModule, MdSelectModule,
  MdSlideToggleModule,
  MdSidenavModule, MdSnackBarModule,
  MdToolbarModule, MdTooltipModule,
  MdDatepickerModule,
  MdNativeDateModule,
  DateAdapter
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
    MdChipsModule,
    MdDatepickerModule,
    MdNativeDateModule
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
    MdChipsModule,
    MdDatepickerModule,
    MdNativeDateModule
  ],
    providers: [
    {provide: DateAdapter, useClass: MyDateAdapter},

  ]
})
export class ApkMaterialModule { }
