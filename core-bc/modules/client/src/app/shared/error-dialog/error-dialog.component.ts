import {Component, Optional} from '@angular/core';
import {MatDialogRef} from '@angular/material';

@Component({
    styleUrls: ['./error-dialog.component.css'],
    templateUrl: './error-dialog.component.html'
})
export class ErrorDialogComponent {
    err: any;
    showDetails = false;
    public dialogRef: MatDialogRef<ErrorDialogComponent>;

    constructor(@Optional() dialogRef: MatDialogRef<ErrorDialogComponent>) {
        this.dialogRef = dialogRef;
    }

    public toggleDetails(): void {
        this.showDetails = !this.showDetails;
    }

    public close(): void {
        this.dialogRef.close();
    }

    public getErrorMessage() {
      try {
        return this.err.json().errorMessage;
      } catch (e) {
        return null;
      }
    }
}
