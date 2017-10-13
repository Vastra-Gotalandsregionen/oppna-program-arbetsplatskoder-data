import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanDeactivate, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs/Observable';
import {ApkEditComponent} from "../apk-edit/apk-edit.component";
import {ConfirmDialogComponent} from "../../shared/confirm-dialog/confirm-dialog.component";
import {MatDialog} from "@angular/material";
import {Subject} from 'rxjs/Subject';
import {ApkDetailComponent} from "../apk-detail/apk-detail.component";

@Injectable()
export class FormChangedGuard implements CanDeactivate<ApkEditComponent> {

  constructor(private dialog: MatDialog) {}

  canDeactivate(component: ApkDetailComponent,
                currentRoute: ActivatedRouteSnapshot,
                currentState: RouterStateSnapshot,
                nextState?: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {

    if (!component.apkFormComponent.apkForm.pristine) {
      let dialogRef = this.dialog.open(ConfirmDialogComponent, {
        data: {
          text: 'Du har icke sparade ändringar. Vill du lämna sidan utan att spara?',
          confirmButtonText: 'Ja - lämna sidan',
          cancelButtonText: 'Nej'
        },
        panelClass: 'apk-dialog'
      });

      let $result = new Subject<boolean>();

      dialogRef.afterClosed().subscribe(result => {
        if (result === 'confirm') {
          $result.next(true);
        } else {
          $result.next(false);
        }
      });

      return $result;
    } else {
      return true;
    }

  }

}
