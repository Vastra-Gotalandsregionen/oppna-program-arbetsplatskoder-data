import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {Prodn3ListComponent} from './prodn3-list/prodn3-list.component';
import {Prodn3EditComponent} from './prodn3-edit/prodn3-edit.component';

const routes: Routes = [
  {
    path: '',
    component: Prodn3ListComponent
  },
  {
    path: ':id/edit',
    component: Prodn3EditComponent
  },
  {
    path: 'create',
    component: Prodn3EditComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class Prodn3RoutingModule { }
