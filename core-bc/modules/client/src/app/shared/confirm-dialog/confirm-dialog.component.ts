import {Component, Optional} from '@angular/core';
import {MdDialogRef} from '@angular/material';

@Component({
    styleUrls: ['./confirm-dialog.component.css'],
    templateUrl: './confirm-dialog.component.html'
})
export class ConfirmDialogComponent {

  public dialogRef: MdDialogRef<ConfirmDialogComponent>;

    constructor(@Optional() dialogRef: MdDialogRef<ConfirmDialogComponent>) {
        this.dialogRef = dialogRef;
    }

}
