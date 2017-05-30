import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {VerksamhetListComponent} from "./verksamhet-list/verksamhet-list.component";
import {VerksamhetEditComponent} from "./verksamhet-edit/verksamhet-edit.component";

const routes: Routes = [
  {
    path: '',
    component: VerksamhetListComponent,
  },
  {
    path: ':id/edit',
    component: VerksamhetEditComponent,
  },
  {
    path: 'create',
    component: VerksamhetEditComponent,
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class VerksamhetRoutingModule { }
