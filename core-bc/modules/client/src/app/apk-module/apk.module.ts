import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {ApkComponent} from "./apk/apk.component";
import {ApkDetailComponent} from "./apk-detail/apk-detail.component";
import {ApkEditComponent} from "./apk-edit/apk-edit.component";
import {ApkCreateComponent} from "./apk-create/apk-create.component";
import {ApkFormComponent} from "./apk-form/apk-form.component";
import {ReactiveFormsModule} from "@angular/forms";
import {ApkMaterialModule} from "./apk-material/apk-material.module";
import {RouterModule} from "@angular/router";
import {ApkRoutingModule} from "./apk-routing.module";

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ApkMaterialModule,
    RouterModule,
    ApkRoutingModule
  ],
  declarations: [
    ApkComponent,
    ApkDetailComponent,
    ApkEditComponent,
    ApkCreateComponent,
    ApkFormComponent
  ]
})
export class ApkModule { }
