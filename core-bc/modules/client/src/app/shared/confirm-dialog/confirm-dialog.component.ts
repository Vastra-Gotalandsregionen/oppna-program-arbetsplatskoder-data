import {Component, Optional, Inject} from '@angular/core';
import {MdDialogRef} from '@angular/material';
import {MD_DIALOG_DATA} from '@angular/material';

@Component({
    styleUrls: ['./confirm-dialog.component.css'],
    templateUrl: './confirm-dialog.component.html'
})
export class ConfirmDialogComponent {

  public dialogRef: MdDialogRef<ConfirmDialogComponent>;

    constructor(@Optional() dialogRef: MdDialogRef<ConfirmDialogComponent>,
                @Inject(MD_DIALOG_DATA) public data: any) {
        this.dialogRef = dialogRef;
    }

}
