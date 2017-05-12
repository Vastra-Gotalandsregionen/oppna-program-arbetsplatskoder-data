import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {Ao3ListComponent} from "./ao3-list/ao3-list.component";
import {Ao3EditComponent} from "./ao3-edit/ao3-edit.component";

const routes: Routes = [
  {
    path: '',
    component: Ao3ListComponent
  },
  {
    path: 'create',
    component: Ao3EditComponent
  },
  {
    path: ':id/edit',
    component: Ao3EditComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class Ao3RoutingModule { }
