import {Injectable} from "@angular/core";
import {ErrorDialogComponent} from "./error-dialog/error-dialog.component";
import {MdDialog, MdDialogRef} from "@angular/material";

@Injectable()
export class ErrorHandler {

  constructor(private dialog: MdDialog) {
  }

  public notifyError(error: any): void {
    let dialogRef: MdDialogRef<ErrorDialogComponent> = this.dialog.open(ErrorDialogComponent);
    dialogRef.componentInstance.err = error;
  }
}
