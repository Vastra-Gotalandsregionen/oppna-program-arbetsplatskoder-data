import {NgModule} from "@angular/core";
import {ApkComponent} from "./apk/apk.component";
import {ApkDetailComponent} from "./apk-detail/apk-detail.component";
import {ApkEditComponent} from "./apk-edit/apk-edit.component";
import {ApkCreateComponent} from "./apk-create/apk-create.component";
import {ApkFormComponent} from "./apk-form/apk-form.component";
import {ApkRoutingModule} from "./apk-routing.module";
import {SharedModule} from "../shared/shared.module";
import {UserHasDataPermissionGuard} from "./guard/user-has-data-permission.guard";

@NgModule({
  imports: [
    ApkRoutingModule,
    SharedModule
  ],
  declarations: [
    ApkComponent,
    ApkDetailComponent,
    ApkEditComponent,
    ApkCreateComponent,
    ApkFormComponent
  ],
  providers: [
    UserHasDataPermissionGuard
  ]
})
export class ApkModule { }
