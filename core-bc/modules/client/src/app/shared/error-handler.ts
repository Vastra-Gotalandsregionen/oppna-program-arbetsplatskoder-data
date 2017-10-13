import {Injectable} from '@angular/core';
import {ErrorDialogComponent} from './error-dialog/error-dialog.component';
import {MatDialog, MatDialogRef} from '@angular/material';

@Injectable()
export class ErrorHandler {

  constructor(private dialog: MatDialog) {
  }

  public notifyError(error: any): void {
    const dialogRef: MatDialogRef<ErrorDialogComponent> = this.dialog.open(ErrorDialogComponent);
    dialogRef.componentInstance.err = error;
  }
}
