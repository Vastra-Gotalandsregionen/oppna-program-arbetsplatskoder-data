import {HomeComponent} from './home/home.component';
import {ApkComponent} from './apk/apk.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {ApkDetailComponent} from './apk-detail/apk-detail.component';
import {ApkEditComponent} from "./apk-edit/apk-edit.component";
import {ApkCreateComponent} from "./apk-create/apk-create.component";

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'home',
        component: HomeComponent
      },
      {
        path: 'apk',
        component: ApkComponent,
      },
      {
        path: 'apk/create',
        component: ApkCreateComponent
      },
      {
        path: 'apk/:id',
        component: ApkDetailComponent
      },
      {
        path: 'apk/:id/edit',
        component: ApkEditComponent
      }
    ]
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
