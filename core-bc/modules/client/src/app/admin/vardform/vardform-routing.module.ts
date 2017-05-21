import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {VardformListComponent} from "./vardform-list/vardform-list.component";
import {VardformEditComponent} from "./vardform-edit/vardform-edit.component";

const routes: Routes = [
  {
    path: '',
    component: VardformListComponent,
  },
  {
    path: ':id/edit',
    component: VardformEditComponent,
  },
  {
    path: 'create',
    component: VardformEditComponent,
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class VardformRoutingModule { }
