import { NgModule } from '@angular/core';
import {MyDateAdapter} from "./my-date-adapter";
import {
  MatAutocompleteModule, MatButtonModule, MatButtonToggleModule,
  MatCardModule, MatCheckboxModule, MatChipsModule, MatDialogModule, MatIconModule,
  MatInputModule,
  MatMenuModule,
  MatListModule, MatProgressBarModule,
  MatRadioModule, MatSelectModule,
  MatSlideToggleModule,
  MatSidenavModule, MatSnackBarModule,
  MatToolbarModule, MatTooltipModule,
  MatDatepickerModule,
  MatNativeDateModule,
  DateAdapter, MAT_DATE_LOCALE
} from '@angular/material';

@NgModule({
  imports: [
    MatAutocompleteModule,
    MatIconModule,
    MatInputModule,
    MatSidenavModule,
    MatToolbarModule,
    MatListModule,
    MatMenuModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatCardModule,
    MatRadioModule,
    MatTooltipModule,
    MatCheckboxModule,
    MatSelectModule,
    MatSlideToggleModule,
    MatSnackBarModule,
    MatDialogModule,
    MatProgressBarModule,
    MatChipsModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  declarations: [],
  exports: [
    MatAutocompleteModule,
    MatIconModule,
    MatInputModule,
    MatSidenavModule,
    MatToolbarModule,
    MatListModule,
    MatMenuModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatCardModule,
    MatRadioModule,
    MatTooltipModule,
    MatCheckboxModule,
    MatSelectModule,
    MatSlideToggleModule,
    MatSnackBarModule,
    MatDialogModule,
    MatProgressBarModule,
    MatChipsModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
    providers: [
    {provide: DateAdapter, useClass: MyDateAdapter},
    {provide: MAT_DATE_LOCALE, useValue: 'sv-SE'}
]


})
export class ApkMaterialModule { }
