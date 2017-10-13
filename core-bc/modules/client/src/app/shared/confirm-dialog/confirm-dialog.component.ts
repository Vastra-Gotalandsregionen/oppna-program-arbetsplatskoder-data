import {Component, Optional, Inject} from '@angular/core';
import {MatDialogRef} from '@angular/material';
import {MAT_DIALOG_DATA} from '@angular/material';

@Component({
    styleUrls: ['./confirm-dialog.component.css'],
    templateUrl: './confirm-dialog.component.html'
})
export class ConfirmDialogComponent {

  public dialogRef: MatDialogRef<ConfirmDialogComponent>;

    constructor(@Optional() dialogRef: MatDialogRef<ConfirmDialogComponent>,
                @Inject(MAT_DIALOG_DATA) public data: any) {
        this.dialogRef = dialogRef;
    }

}
